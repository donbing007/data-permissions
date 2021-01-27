package com.xforceplus.ultraman.permissions.rule.check.common;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.rule.check.select.SelectFieldChecker;
import com.xforceplus.ultraman.permissions.rule.check.select.SelectFieldCheckerTest;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.rule.context.DefaultContext;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.sql.SqlParser;
import com.xforceplus.ultraman.permissions.sql.define.Alias;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.Func;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.JSqlParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * ConditionsFieldChecker Tester.
 *
 * @author <Authors name>
 * @version 1.0 12/04/2019
 * @since <pre>Dec 4, 2019</pre>
 */
public class ConditionsFieldCheckerTest {

    private SqlParser sqlParser = JSqlParser.getInstance();

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }


    /**
     * Method: checkCondition(ConditionAbility conditionAbility, FieldFromAbility fieldFromAbility, Context context)
     */
    @Test
    public void testCheckCondition() throws Exception {
        ConditionsFieldChecker checker = new ConditionsFieldChecker();
        Map<SearchRequest, RuleCheckPack> caseData = buildCase();
        caseData.keySet().stream().forEach(request -> {
            try {

                Searcher searcher = mock(Searcher.class);
                RuleCheckPack pack = caseData.get(request);
                Authorizations authorizations = pack.auths;
                List<FieldRule> rules;
                for (Authorization auth : authorizations.getAuthorizations()) {
                    rules = pack.rules.get(auth.toString() + pack.entity);
                    when(searcher.searchFieldRule(auth, pack.entity)).thenReturn(rules);
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
        private String entity;
        // key = authorization.toString() + entity
        private Map<String, List<FieldRule>> rules;

        public RuleCheckPack(Authorizations auths, String entity, Map<String, List<FieldRule>> rules) {
            this.auths = auths;
            this.entity = entity;
            this.rules = rules;
        }
    }

    private Map<SearchRequest, RuleCheckPack> buildCase() {
        Map<SearchRequest, RuleCheckPack> data = new LinkedHashMap<>();

        data.put(new SearchRequest(
                "SELECT t1.c1 FROM t t1 WHERE (t1.c1 || (t1.c2 || t1.c3)) in ( 'a2','a1')",
                false,
                Collections.emptyList()),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                "t",
                new HashMap() {{
                    put(new Authorization("r1", "t1").toString() + "t",
                        Arrays.asList(
                            new FieldRule("t", "c1"),
                            new FieldRule("t", "c2"),
                            new FieldRule("t", "c3")
                        )
                    );
                }}
            )
        );

        data.put(new SearchRequest(
                "select t.c1, t.c2 from t1 t",
                false,
                Collections.emptyList()),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                "t1",
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
                "select tt.c1, tt.c3 from t1 tt where tt.c1=100 and tt.c2=200",
                true,
                Arrays.asList(
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                "t1",
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
                "select t.c1 from t1 t where t.c1=300",
                true,
                Arrays.asList(
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                "t1",
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
                "select t.c1andc2 from (select (t2.c1+t2.c2) c1andc2 from t2 where t2.c2=100) t where t.c1andc2=100",
                false,
                Arrays.asList(
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                "t2",
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
                "select t.c1andc2 from (select t2.c1, t2.c2, t2.c3 c1andc2 from t2 where t2.c1=30) t",
                true,
                Arrays.asList(
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                "t2",
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
                true,
                Arrays.asList(
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                "t1",
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
                "t1",
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
                "select t1.c2 from t1 where t1.c2=11000",
                true,
                Arrays.asList(
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(new Authorization("r1", "t1"))),
                "t1",
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
                "select t1.c2 from t1 where t1.c2=1123455",
                false,
                Arrays.asList(
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(
                    new Authorization("r1", "t1"),
                    new Authorization("r2", "t1")
                )),
                "t1",
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
                "select t1.c2 from t1 where t1.c2=1123455333",
                true,
                Arrays.asList(
                )),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(
                    new Authorization("r1", "t1"),
                    new Authorization("r2", "t1")
                )),
                "t1",
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
                "select t.count from (select count(t1.c2) count from t1 where t1.c2=100) t where t.count=00000",
                false,
                Arrays.asList()),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(
                    new Authorization("r1", "t1"),
                    new Authorization("r2", "t1")
                )),
                "t1",
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
                "select sum(t.c1, t.c2) as s, t.c3 from (select t1.c1, t1.c2, t1.c3 count from t1 ) t where t.c1=200",
                true,
                Arrays.asList()),
            new RuleCheckPack(
                new Authorizations(Arrays.asList(
                    new Authorization("r1", "t1"),
                    new Authorization("r2", "t1")
                )),
                "t1",
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

        return data;
    }


} 
