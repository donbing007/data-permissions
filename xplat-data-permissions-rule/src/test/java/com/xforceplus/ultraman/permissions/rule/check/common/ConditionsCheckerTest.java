package com.xforceplus.ultraman.permissions.rule.check.common;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.pojo.rule.*;
import com.xforceplus.ultraman.permissions.rule.context.DefaultContext;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.SqlParser;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.JSqlParser;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * checker test.
 */
public class ConditionsCheckerTest {

    private SqlParser sqlParser = JSqlParser.getInstance();

    @Test
    public void check() throws Exception {
        Map<String, ConditionPack> caseData = buildCase();

        ConditionsChecker checker = new ConditionsChecker();
        caseData.keySet().stream().forEach(s -> {
            Sql sql = sqlParser.parser(s);

            Searcher searcher = mock(Searcher.class);

            ConditionPack pack = caseData.get(s);
            pack.ruleMap.keySet().stream().forEach(auth -> {
                Map<String, List<DataRule>> tableRules = pack.ruleMap.get(auth);
                tableRules.keySet().stream().forEach(t -> {
                    when(searcher.searchDataRule(auth, t)).thenReturn(tableRules.get(t));
                });
            });

            DefaultContext context = new DefaultContext(sql,
                new Authorizations(new ArrayList(pack.ruleMap.keySet())), searcher);

            checker.check(context);

            Assert.assertEquals(pack.update, context.isUpdatedSql());
            try {

                Assert.assertEquals(s, CCJSqlParserUtil.parse(pack.expectation).toString(), sql.toSqlString());

            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        });

    }

    private Map<String, ConditionPack> buildCase() throws Exception {
        Map<String, ConditionPack> data = new LinkedHashMap<>();

        data.put(
            "select t1.c1 from t1",
            new ConditionPack(
                new HashMap<Authorization, Map<String, List<DataRule>>>() {{
                    put(new Authorization("r1", "t1"),
                        new HashMap() {{
                            put("t1", Arrays.asList(
                                new DataRule("t1", "c1", Arrays.asList(
                                    new DataRuleCondition(
                                        RuleConditionOperation.EQUAL,
                                        RuleConditionValueType.INTEGER,
                                        RuleConditionRelationship.AND,
                                        "100")
                                ))
                            ));
                        }}
                    );
                }},
                CCJSqlParserUtil.parse("select t1.c1 from t1 where t1.c1=100").toString(),
                true
            )
        );

        data.put(
            "select t1.c10 from t1",
            new ConditionPack(
                new HashMap<Authorization, Map<String, List<DataRule>>>() {{
                    put(new Authorization("r1", "t1"),
                        new HashMap() {{
                            put("t1", Arrays.asList());
                        }}
                    );
                }},
                CCJSqlParserUtil.parse("select t1.c10 from t1").toString(),
                false
            )
        );

        data.put(
            "select t11.c10 from t11",
            new ConditionPack(
                new LinkedHashMap<Authorization, Map<String, List<DataRule>>>() {{

                    put(new Authorization("r2", "t1"),
                        Collections.emptyMap());

                    put(new Authorization("r1", "t1"),
                        new LinkedHashMap() {{
                            put("t11", Arrays.asList(
                                new DataRule("t11", "c1", Arrays.asList(
                                    new DataRuleCondition(
                                        RuleConditionOperation.EQUAL,
                                        RuleConditionValueType.INTEGER,
                                        RuleConditionRelationship.AND,
                                        "100"
                                    )
                                ))
                            ));
                        }}
                    );
                }},
                CCJSqlParserUtil.parse("select t11.c10 from t11 where t11.c1=100").toString(),
                true
            )
        );

        data.put(
            "select t111.c10 from t111",
            new ConditionPack(
                new LinkedHashMap<Authorization, Map<String, List<DataRule>>>() {{

                    put(new Authorization("r2", "t1"), Collections.emptyMap());

                    put(new Authorization("r1", "t1"), Collections.emptyMap());

                }},
                CCJSqlParserUtil.parse("select t111.c10 from t111").toString(),
                false
            )
        );

        data.put(
            "select * from t1",
            new ConditionPack(
                new HashMap<Authorization, Map<String, List<DataRule>>>() {{
                    put(new Authorization("r1", "t1"),
                        new HashMap() {{
                            put("t1", Arrays.asList(
                                new DataRule("t1", "c1", Arrays.asList(
                                    new DataRuleCondition(
                                        RuleConditionOperation.EQUAL,
                                        RuleConditionValueType.INTEGER,
                                        RuleConditionRelationship.AND,
                                        "100")
                                ))
                            ));
                        }}
                    );

                    put(new Authorization("r2", "t1"),
                        new HashMap() {{
                            put("t1", Arrays.asList(
                                new DataRule("t1", "c1", Arrays.asList(
                                    new DataRuleCondition(
                                        RuleConditionOperation.NOT_EQUAL,
                                        RuleConditionValueType.INTEGER,
                                        RuleConditionRelationship.AND,
                                        "3000")
                                ))
                            ));
                        }}
                    );
                }},
                CCJSqlParserUtil.parse("select * from t1 where (t1.c1=100) or (t1.c1 != 3000)").toString(),
                true
            )
        );

        data.put(
            "select * from t1 where t1.c1=5000",
            new ConditionPack(
                new HashMap<Authorization, Map<String, List<DataRule>>>() {{
                    put(new Authorization("r1", "t1"),
                        new HashMap() {{
                            put("t1", Arrays.asList(
                                new DataRule("t1", "c1", Arrays.asList(
                                    new DataRuleCondition(
                                        RuleConditionOperation.EQUAL,
                                        RuleConditionValueType.INTEGER,
                                        RuleConditionRelationship.AND,
                                        "100")
                                ))
                            ));
                        }}
                    );

                    put(new Authorization("r2", "t1"),
                        new HashMap() {{
                            put("t1", Arrays.asList(
                                new DataRule("t1", "c1", Arrays.asList(
                                    new DataRuleCondition(
                                        RuleConditionOperation.NOT_EQUAL,
                                        RuleConditionValueType.INTEGER,
                                        RuleConditionRelationship.AND,
                                        "3000")
                                ))
                            ));
                        }}
                    );
                }},
                CCJSqlParserUtil.parse("select * from t1 where (t1.c1=5000) and ((t1.c1=100) or (t1.c1 != 3000))").toString(),
                true
            )
        );


        data.put(
            "select t1.* from t1 inner join t2 on t1.c1=t2.c1",
            new ConditionPack(
                new HashMap<Authorization, Map<String, List<DataRule>>>() {{
                    put(new Authorization("r1", "t1"),
                        new HashMap() {{
                            put("t1", Arrays.asList(
                                new DataRule("t1", "c1", Arrays.asList(
                                    new DataRuleCondition(
                                        RuleConditionOperation.EQUAL,
                                        RuleConditionValueType.INTEGER,
                                        RuleConditionRelationship.AND,
                                        "100")
                                ))
                            ));

                            put("t2", Arrays.asList(
                                new DataRule("t2", "c1", Arrays.asList(
                                    new DataRuleCondition(
                                        RuleConditionOperation.EQUAL,
                                        RuleConditionValueType.INTEGER,
                                        RuleConditionRelationship.AND,
                                        "100")
                                ))
                            ));

                        }}
                    );

                }},
                CCJSqlParserUtil.parse("select t1.* from t1 inner join t2 on t1.c1=t2.c1 where t1.c1=100 and t2.c1=100").toString(),
                true
            )
        );

        return data;
    }

    private static class ConditionPack {
        private String expectation;
        // 一个授权下有多个表的多个权限.
        private Map<Authorization, Map<String, List<DataRule>>> ruleMap;
        private boolean update;

        public ConditionPack(
            Map<Authorization, Map<String, List<DataRule>>> ruleMap, String expectedSql, boolean update) {
            this.expectation = expectedSql;
            this.ruleMap = ruleMap;
            this.update = update;
        }
    }
}
