package com.xforceplus.ultraman.permissions.rule.check.insert;

import com.xforceplus.ultraman.perissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.perissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.perissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.rule.context.DefaultContext;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.sql.SqlParser;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.JSqlParser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * InsertFieldChecker Tester.
 *
 * @author <Authors name>
 * @version 1.0 11/13/2019
 * @since <pre>Nov 13, 2019</pre>
 */
public class InsertFieldCheckerTest {

    private Authorization auth = new Authorization("r1", "t1");
    private SqlParser sqlParser = JSqlParser.getInstance();

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: checkTypeSafe(CheckContext context)
     */
    @Test
    public void testCheck() throws Exception {
        Map<String, Pack> caseData = buildCase();

        InsertFieldChecker checker = new InsertFieldChecker();
        caseData.keySet().stream().forEach(sql -> {

            Searcher searcher = mock(Searcher.class);
            Pack pack = caseData.get(sql);
            pack.ruleTable.entrySet().stream().forEach(e -> {
                when(searcher.searchFieldRule(auth, e.getKey())).thenReturn(e.getValue());
            });

            Context context = new DefaultContext(sqlParser.parser(sql), auth, searcher);
            checker.check(context);

            Assert.assertEquals(sql, pack.expected, context.isRefused());
        });
    }

    private Map<String, Pack> buildCase() {
        Map<String, Pack> data = new LinkedHashMap<>();

        data.put("insert into t1 (c1,c2) value(1,2)",
            new Pack(
                false,
                new HashMap<String, List<FieldRule>>() {{
                    put("t1",
                        Arrays.asList(
                            new FieldRule("t1", "c1"),
                            new FieldRule("t1", "c2")
                        )
                    );
                }}
            )
        );
        data.put("insert into t1 (c1,c2) value(1,2)",
            new Pack(
                true,
                new HashMap<String, List<FieldRule>>() {{
                    put("t2",
                        Arrays.asList(
                            new FieldRule("t2", "c1"),
                            new FieldRule("t2", "c2")
                        )
                    );
                }}
            )
        );
        data.put("insert into t1 (c1,c2) value(1,2)",
            new Pack(
                true,
                new HashMap<String, List<FieldRule>>() {{
                    put("t1",
                        Arrays.asList(
                            new FieldRule("t1", "c1")
                        )
                    );
                }}
            )
        );

        return data;
    }


    private static class Pack {
        private boolean expected;
        private Map<String, List<FieldRule>> ruleTable;

        public Pack(boolean expected, Map<String, List<FieldRule>> ruleTable) {
            this.expected = expected;
            this.ruleTable = ruleTable;
        }
    }
}
