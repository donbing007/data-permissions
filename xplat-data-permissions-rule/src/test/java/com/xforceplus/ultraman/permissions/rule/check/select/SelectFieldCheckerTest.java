package com.xforceplus.ultraman.permissions.rule.check.select;


import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.rule.context.DefaultContext;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.sql.SqlParser;
import com.xforceplus.ultraman.permissions.sql.define.Alias;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.Func;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.JSqlParser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * checker test.
 */
public class SelectFieldCheckerTest {

    private SqlParser sqlParser = JSqlParser.getInstance();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void check() {

        SelectFieldChecker checker = new SelectFieldChecker();
        Map<SearchRequest, RuleCheckPack> caseData = buildCase();
        caseData.keySet().stream().forEach(request -> {
            try {

                Searcher searcher = mock(Searcher.class);
                RuleCheckPack pack = caseData.get(request);
                Authorizations authorizations = pack.auths;
                List<FieldRule> rules;
                for (Authorization auth : authorizations.getAuthorizations()) {
                    for (String entity : pack.entitys) {
                        rules = pack.rules.get(auth.toString() + entity);
                        when(searcher.searchFieldRule(auth, entity)).thenReturn(rules);
                    }
                }

                Context context = new DefaultContext(sqlParser.parser(request.sql), pack.auths, searcher);
                checker.check(context);

                Assert.assertEquals(request.sql, request.refused, context.isRefused());
                Assert.assertArrayEquals(request.sql, request.blackList.toArray(new Item[0]), context.blackList());


            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        });

    }

    private static class SearchRequest {
        private String sql;
        private boolean refused;
        private List<Item> blackList;

        public SearchRequest(String sql, boolean refused, List<Item> blackList) {
            this.sql = sql;
            this.refused = refused;
            this.blackList = blackList;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SearchRequest)) return false;
            SearchRequest that = (SearchRequest) o;
            return refused == that.refused &&
                Objects.equals(sql, that.sql) &&
                Objects.equals(blackList, that.blackList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sql, refused, blackList);
        }
    }

    private static class RuleCheckPack {
        private Authorizations auths;
        private List<String> entitys;
        // key = authorization.toString() + entity
        private Map<String, List<FieldRule>> rules;

        public RuleCheckPack(Authorizations auths, List<String> entitys, Map<String, List<FieldRule>> rules) {
            this.auths = auths;
            this.entitys = entitys;
            this.rules = rules;
        }
    }

    private Map<SearchRequest, RuleCheckPack> buildCase() {
        Map<SearchRequest, RuleCheckPack> data = new LinkedHashMap<>();

        data.put(new SearchRequest(
                "select t.c1, t.c2 from t1 t",
                false,
                Collections.emptyList()),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                Arrays.asList("t1"),
                new HashMap() {{
                    put(new Authorization("r1", "t1").toString() + "t1",
                        Arrays.asList(
                            new FieldRule("t1", "c1"),
                            new FieldRule("t1", "c2"),
                            new FieldRule("t1", "c3")
                        )
                    );
                }}
            )
        );

        data.put(new SearchRequest(
                "select tt.c1, tt.c3 from t1 tt",
                false,
                Arrays.asList(
                    new Field("tt", "c1")
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                Arrays.asList("t1"),
                new HashMap() {{
                    put(new Authorization("r1", "t1").toString() + "t1",
                        Arrays.asList(
                            new FieldRule("t1", "c3"),
                            new FieldRule("t1", "c4"),
                            new FieldRule("t1", "c5")
                        )
                    );
                }}
            )
        );

        data.put(new SearchRequest(
                "select t.c1 from t1 t",
                true,
                Arrays.asList(
                    new Field("t", "c1")
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                Arrays.asList("t1"),
                new HashMap() {{
                    put(new Authorization("r1", "t1").toString() + "t1",
                        Arrays.asList(
                            new FieldRule("t1", "c3"),
                            new FieldRule("t1", "c4"),
                            new FieldRule("t1", "c5")
                        )
                    );
                }}
            )
        );

        data.put(new SearchRequest(
                "select t.c1andc2 from t1 (select (t2.c1+t2.c2) c1andc2 from t2)",
                false,
                Arrays.asList(
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                Arrays.asList("t2"),
                new HashMap() {{
                    put(new Authorization("r1", "t1").toString() + "t2",
                        Arrays.asList(
                            new FieldRule("t2", "c1"),
                            new FieldRule("t2", "c2"),
                            new FieldRule("t2", "c3")
                        )
                    );
                }}
            )
        );

        data.put(new SearchRequest(
                "select t.c1andc2 from (select (t2.c1+t2.c2) c1andc2 from t2) t",
                true,
                Arrays.asList(
                    new Field("t", "c1andc2")
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                Arrays.asList("t2"),
                new HashMap() {{
                    put(
                        new Authorization("r1", "t1").toString() + "t2",
                        Arrays.asList(
                            new FieldRule("t2", "c2"),
                            new FieldRule("t2", "c3")
                        )
                    );
                }}
            )
        );

        data.put(new SearchRequest(
                "select t1.c2 from t1 where t1.c1=1111",
                false,
                Arrays.asList(
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                Arrays.asList("t1"),
                new HashMap() {{
                    put(
                        new Authorization("r1", "t1").toString() + "t1",
                        Arrays.asList(
                            new FieldRule("t1", "c2"),
                            new FieldRule("t1", "c3")
                        )
                    );
                }}
            )
        );

        data.put(new SearchRequest(
                "select t1.c2 from t1 where t1.c1=10",
                false,
                Arrays.asList(
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                Arrays.asList("t1"),
                new HashMap() {{
                    put(
                        new Authorization("r1", "t1").toString() + "t1",
                        Arrays.asList(
                            new FieldRule("t1", "*")
                        )
                    );
                }}
            )
        );

        data.put(new SearchRequest(
                "select t1.c2 from t1 where t1.c1=11000",
                true,
                Arrays.asList(
                    new Field("t1", "c2")
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                Arrays.asList("t1"),
                new HashMap() {{
                    put(
                        new Authorization("r1", "t1").toString() + "t1",
                        Arrays.asList(
                            new FieldRule("t1", "*"),
                            new FieldRule("t1", "c3")
                        )
                    );
                }}
            )
        );

        data.put(new SearchRequest(
                "select t1.c2 from t1 where t1.c1=1123455",
                false,
                Arrays.asList(
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(
                    new Authorization("r1", "t1"),
                    new Authorization("r2", "t1")
                )),
                Arrays.asList("t1"),
                new HashMap() {{
                    put(
                        new Authorization("r1", "t1").toString() + "t1",
                        Arrays.asList(
                            new FieldRule("t1", "*")
                        )
                    );

                    put(
                        new Authorization("r2", "t1").toString() + "t1",
                        Collections.emptyList()
                    );
                }}
            )
        );

        data.put(new SearchRequest(
                "select t1.c2 from t1 where t1.c1=1123455333",
                true,
                Arrays.asList(
                    new Field("t1", "c2")
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(
                    new Authorization("r1", "t1"),
                    new Authorization("r2", "t1")
                )),
                Arrays.asList("t1"),
                new HashMap() {{
                    put(
                        new Authorization("r1", "t1").toString() + "t1",
                        Collections.emptyList()
                    );

                    put(
                        new Authorization("r2", "t1").toString() + "t1",
                        Collections.emptyList()
                    );
                }}
            )
        );

        data.put(new SearchRequest(
                "select count(t1.c2) as a from t1 where t1.c1=00000",
                true,
                Arrays.asList(
                    new Func("count", Arrays.asList(new Field("t1", "c2")), new Alias("a", true))
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(
                    new Authorization("r1", "t1"),
                    new Authorization("r2", "t1")
                )),
                Arrays.asList("t1"),
                new HashMap() {{
                    put(
                        new Authorization("r1", "t1").toString() + "t1",
                        Collections.emptyList()
                    );

                    put(
                        new Authorization("r2", "t1").toString() + "t1",
                        Collections.emptyList()
                    );
                }}
            )
        );

        data.put(new SearchRequest(
                "select t.count from (select count(t1.c2) count from t1 ) t where t.count=00000",
                false,
                Arrays.asList()),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(
                    new Authorization("r1", "t1"),
                    new Authorization("r2", "t1")
                )),
                Arrays.asList("t1"),
                new HashMap() {{
                    put(
                        new Authorization("r1", "t1").toString() + "t1",
                        Arrays.asList(
                            new FieldRule("t1", "c2")
                        )
                    );

                    put(
                        new Authorization("r2", "t1").toString() + "t1",
                        Collections.emptyList()
                    );
                }}
            )
        );

        data.put(new SearchRequest(
                "select sum(t.c1, t.c2) as s, t.c3 from (select t1.c1, t1.c2, t1.c3 count from t1 ) t",
                false,
                Arrays.asList(
                    new Func(
                        "sum",
                        Arrays.asList(
                            new Field("t", "c1"),
                            new Field("t", "c2")
                        ),
                        new Alias("s", true)
                    )
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(
                    new Authorization("r1", "t1"),
                    new Authorization("r2", "t1")
                )),
                Arrays.asList("t1"),
                new HashMap() {{
                    put(
                        new Authorization("r1", "t1").toString() + "t1",
                        Arrays.asList(
                            new FieldRule("t1", "c5")
                        )
                    );

                    put(
                        new Authorization("r2", "t1").toString() + "t1",
                        Collections.emptyList()
                    );
                }}
            )
        );

        data.put(new SearchRequest(
                "select distinct summ.auth_tax_period as auth_tax_period from crp_purchaser_auth_summ summ " +
                    "where summ.auth_tax_period is not null and summ.auth_tax_period <> '' " +
                    "union all " +
                    "select s.name as auth_tax_period from dim_auth_charge_status s where s.type = 'noAuth'",
                false,
                Arrays.asList()),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                Arrays.asList("crp_purchaser_auth_summ", "dim_auth_charge_status"),
                new HashMap() {{
                    put(
                        new Authorization("r1", "t1").toString() + "crp_purchaser_auth_summ",
                        Arrays.asList(
                            new FieldRule("crp_purchaser_auth_summ", "*")
                        )
                    );

                    put(
                        new Authorization("r1", "t1").toString() + "dim_auth_charge_status",
                        Arrays.asList(
                            new FieldRule("dim_auth_charge_status", "*")
                        )
                    );

                }}
            )
        );

        data.put(new SearchRequest(
                "select summ.auth_tax_period as auth_tax_period from crp_purchaser_auth_summ summ " +
                    "where summ.auth_tax_period is not null and summ.auth_tax_period <> '' " +
                    "union all " +
                    "select s.name as auth_tax_period from dim_auth_charge_status s where s.type = 'noAuth'",
                true,
                Arrays.asList(
                    new Field("s", "name", new Alias("auth_tax_period", true))
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                Arrays.asList("crp_purchaser_auth_summ", "dim_auth_charge_status"),
                new HashMap() {{
                    put(
                        new Authorization("r1", "t1").toString() + "crp_purchaser_auth_summ",
                        Arrays.asList(
                            new FieldRule("crp_purchaser_auth_summ", "*")
                        )
                    );

                }}
            )
        );

        data.put(new SearchRequest(
                "select t.c1 from (select t.c1 from t1 t union select t.c1 from t2 t) t",
                true,
                Arrays.asList(new Field("t", "c1"))),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                Arrays.asList("t1", "t2"),
                new HashMap() {{
                    put(
                        new Authorization("r1", "t1").toString() + "t1",
                        Arrays.asList(
                            new FieldRule("t1", "c1")
                        )
                    );

                    put(
                        new Authorization("r1", "t1").toString() + "t2",
                        Arrays.asList(
                            new FieldRule("t2", "c2")
                        )
                    );

                }}
            )
        );

        data.put(new SearchRequest(
                "SELECT IFNULL(t.authCode, '已勾选') AS authCode, it.invoiceType AS invoiceType, it.invoiceName , " +
                    "IFNULL(t.total, 0) AS total , IFNULL(t.tax_amount, 0) AS tax_amount , IFNULL(t.amount_without_tax, 0)" +
                    " AS amount_without_tax FROM ( SELECT '已勾选' AS authCode, t1.invoice_type, COUNT(1) AS total , " +
                    "SUM(t1.tax_amount) AS tax_amount, SUM(t1.amount_without_tax) AS amount_without_tax FROM " +
                    "auth.aut_tax_invoice t1 WHERE (t1.auth_response_time >= 1593532800 AND t1.auth_response_time " +
                    "<= 1596124800 AND t1.auth_status IN (4, 7, 8)) GROUP BY t1.invoice_type ) t RIGHT JOIN ( " +
                    "SELECT 's' AS invoiceType, '专用发票' AS invoiceName FROM auth.aut_tax_invoice ts UNION SELECT 'v' " +
                    "AS invoiceType, '机动车统一销售发票' AS invoiceName FROM auth.aut_tax_invoice tv UNION SELECT 'ct' " +
                    "AS invoiceType, '通行费电子发票' AS invoiceName FROM auth.aut_tax_invoice tct UNION SELECT 'hg' " +
                    "AS invoiceType, '海关缴款文书' AS invoiceName FROM auth.aut_tax_invoice thg ) it ON t.invoice_type" +
                    " = it.invoiceType UNION ALL SELECT IFNULL(t.authCode, '未勾选') AS authCode, it.invoiceType AS " +
                    "invoiceType, it.invoiceName , IFNULL(t.total, 0) AS total , IFNULL(t.tax_amount, 0) AS tax_amount " +
                    ", IFNULL(t.amount_without_tax, 0) AS amount_without_tax FROM ( SELECT '未勾选' AS authCode, " +
                    "t2.invoice_type, COUNT(1) AS total , SUM(t2.tax_amount) AS tax_amount, SUM(t2.amount_without_tax) " +
                    "AS amount_without_tax FROM auth.aut_tax_invoice t2 WHERE t2.auth_status IN (0, 1, 2, 3, 5, 6) " +
                    "GROUP BY t2.invoice_type ) t RIGHT JOIN ( SELECT 's' AS invoiceType, '专用发票' AS invoiceName " +
                    "FROM auth.aut_tax_invoice ts UNION SELECT 'v' AS invoiceType, '机动车统一销售发票' AS invoiceName " +
                    "FROM auth.aut_tax_invoice tv UNION SELECT 'ct' AS invoiceType, '通行费电子发票' AS invoiceName " +
                    "FROM auth.aut_tax_invoice tct UNION SELECT 'hg' AS invoiceType, '海关缴款文书' AS invoiceName " +
                    "FROM auth.aut_tax_invoice thg ) it ON t.invoice_type = it.invoiceType UNION ALL SELECT " +
                    "IFNULL(t.authCode, '不抵扣') AS authCode, it.invoiceType AS invoiceType, it.invoiceName , " +
                    "IFNULL(t.total, 0) AS total , IFNULL(t.tax_amount, 0) AS tax_amount , IFNULL(t.amount_without_tax," +
                    " 0) AS amount_without_tax FROM ( SELECT '不抵扣' AS authCode, t3.invoice_type, COUNT(1) AS total , " +
                    "SUM(t3.tax_amount) AS tax_amount, SUM(t3.amount_without_tax) AS amount_without_tax FROM " +
                    "auth.aut_tax_invoice t3 WHERE (t3.auth_response_time >= 1593532800 AND t3.auth_response_time <= " +
                    "1596124800 AND t3.auth_use = 2) GROUP BY t3.invoice_type ) t RIGHT JOIN ( SELECT 's' AS invoiceType," +
                    " '专用发票' AS invoiceName FROM auth.aut_tax_invoice ts UNION SELECT 'v' AS invoiceType, " +
                    "'机动车统一销售发票' AS invoiceName FROM auth.aut_tax_invoice tv UNION SELECT 'ct' AS invoiceType, " +
                    "'通行费电子发票' AS invoiceName FROM auth.aut_tax_invoice ct UNION SELECT 'hg' AS invoiceType, " +
                    "'海关缴款文书' AS invoiceName ) it ON t.invoice_type = it.invoiceType",
                false,
                Arrays.asList()),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                Arrays.asList("aut_tax_invoice"),
                new HashMap() {{
                    put(
                        new Authorization("r1", "t1").toString() + "aut_tax_invoice",
                        Arrays.asList(
                            new FieldRule("aut_tax_invoice", "*")
                        )
                    );
                }}
            )
        );

        return data;
    }
}
