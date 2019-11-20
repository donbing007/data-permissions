package com.xforceplus.ultraman.permissions.rule.check.common;

import com.xforceplus.ultraman.perissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.perissions.pojo.rule.*;
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

    private Authorization auth = new Authorization("r1", "t1");
    private SqlParser sqlParser = JSqlParser.getInstance();

    @Test
    public void check() throws Exception {
        Map<String, ConditionPack> caseData = buildCase();

        ConditionsChecker checker = new ConditionsChecker();
        caseData.keySet().stream().forEach(s -> {
            Sql sql = sqlParser.parser(s);

            Searcher searcher = mock(Searcher.class);

            ConditionPack pack = caseData.get(s);
            pack.ruleMap.keySet().stream().forEach(e -> {
                when(searcher.searchDataRule(auth, e)).thenReturn(pack.ruleMap.get(e));
            });

            DefaultContext context = new DefaultContext(sql, auth, searcher);

            checker.check(context);

            Assert.assertTrue(context.isUpdatedSql());
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
            "select * from t1",
            new ConditionPack(
                new HashMap<String, List<DataRule>>() {{
                    put("t1", Arrays.asList(
                        new DataRule("t1", "c1", Arrays.asList(
                            new DataRuleCondition(
                                RuleConditionOperation.EQUAL,
                                RuleConditionValueType.INTEGER,
                                RuleConditionRelationship.AND,
                                "100")
                        ))
                    ));
                }},
                CCJSqlParserUtil.parse("select * from t1 where (t1.c1=100)").toString()
            )
        );

        data.put(
            "select * from t1",
            new ConditionPack(
                new HashMap<String, List<DataRule>>() {{
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

                }},
                CCJSqlParserUtil.parse("select * from t1 where (t1.c1=100) and (t2.c1=100)").toString()
            )
        );

        data.put(
            "select * from t1",
            new ConditionPack(
                new HashMap<String, List<DataRule>>() {{
                    put("t1", Arrays.asList(
                        new DataRule("t1", "c1", Arrays.asList(
                            new DataRuleCondition(
                                RuleConditionOperation.EQUAL,
                                RuleConditionValueType.INTEGER,
                                RuleConditionRelationship.AND,
                                "100"),
                            new DataRuleCondition(
                                RuleConditionOperation.LIST,
                                RuleConditionValueType.INTEGER,
                                RuleConditionRelationship.OR,
                                "100,200")
                        ))
                    ));
                }},
                CCJSqlParserUtil.parse("select * from t1 where (t1.c1=100 or t1.c1 in (100,200))").toString()
            )
        );

        data.put(
            "select * from (select * from t1)",
            new ConditionPack(
                new HashMap<String, List<DataRule>>() {{
                    put("t1", Arrays.asList(
                        new DataRule("t1", "c1", Arrays.asList(
                            new DataRuleCondition(
                                RuleConditionOperation.EQUAL,
                                RuleConditionValueType.INTEGER,
                                RuleConditionRelationship.AND,
                                "100"),
                            new DataRuleCondition(
                                RuleConditionOperation.LIST,
                                RuleConditionValueType.INTEGER,
                                RuleConditionRelationship.OR,
                                "100,200")
                        ))
                    ));
                }},
                CCJSqlParserUtil.parse("select * from (select * from t1 where (t1.c1=100 or t1.c1 in (100,200)))").toString()
            )
        );

        data.put(
            "select * from (select * from t1 where t1.c1=300)",
            new ConditionPack(
                new HashMap<String, List<DataRule>>() {{
                    put("t1", Arrays.asList(
                        new DataRule("t1", "c1", Arrays.asList(
                            new DataRuleCondition(
                                RuleConditionOperation.EQUAL,
                                RuleConditionValueType.INTEGER,
                                RuleConditionRelationship.AND,
                                "100"),
                            new DataRuleCondition(
                                RuleConditionOperation.LIST,
                                RuleConditionValueType.INTEGER,
                                RuleConditionRelationship.OR,
                                "100,200")
                        ))
                    ));
                }},
                CCJSqlParserUtil.parse("select * from (select * from t1 where (t1.c1=300) and (t1.c1=100 or t1.c1 in (100,200)))").toString()
            )
        );

        data.put(
            "update t1 set t1.c1=200",
            new ConditionPack(
                new HashMap<String, List<DataRule>>() {{
                    put("t1", Arrays.asList(
                        new DataRule("t1", "c1", Arrays.asList(
                            new DataRuleCondition(
                                RuleConditionOperation.EQUAL,
                                RuleConditionValueType.INTEGER,
                                RuleConditionRelationship.AND,
                                "100"),
                            new DataRuleCondition(
                                RuleConditionOperation.LIST,
                                RuleConditionValueType.INTEGER,
                                RuleConditionRelationship.OR,
                                "100,200")
                        ))
                    ));
                }},
                CCJSqlParserUtil.parse("update t1 set t1.c1=200 where (t1.c1=100 or t1.c1 in (100,200))").toString()
            )
        );

        data.put(
            "update t1 set t1.c1=200",
            new ConditionPack(
                new HashMap<String, List<DataRule>>() {{
                    put("t1", Arrays.asList(
                        new DataRule("t1", "c1", Arrays.asList(
                            new DataRuleCondition(
                                RuleConditionOperation.EQUAL,
                                RuleConditionValueType.INTEGER,
                                RuleConditionRelationship.AND,
                                "100")
                        )),
                        new DataRule("t1", "c2", Arrays.asList(
                            new DataRuleCondition(
                                RuleConditionOperation.AFTER,
                                RuleConditionValueType.STRING,
                                RuleConditionRelationship.AND,
                                "test")
                        ))
                    ));
                }},
                CCJSqlParserUtil.parse("update t1 set t1.c1=200 where (t1.c1=100) and (t1.c2 like '%test')").toString()
            )
        );

        return data;
    }

    private static class ConditionPack {
        private String expectation;
        private Map<String, List<DataRule>> ruleMap;

        public ConditionPack(Map<String, List<DataRule>> ruleMap, String expectedSql) {
            this.expectation = expectedSql;
            this.ruleMap = ruleMap;
        }
    }
}
