package com.xforceplus.ultraman.permissions.rule.check.update;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.rule.context.DefaultContext;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.sql.SqlParser;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.JSqlParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * UpdateSetFieldChecker Tester.
 *
 * @author dongbin
 * @version 1.0 11/12/2019
 * @since <pre>Nov 12, 2019</pre>
 */
public class UpdateSetFieldCheckerTest {

    private Authorization auth = new Authorization("r1", "t1");
    private SqlParser sqlParser = JSqlParser.getInstance();

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: check(CheckContext context)
     */
    @Test
    public void testCheck() throws Exception {
        Map<String, Pack> caseData = buildCase();

        UpdateSetFieldChecker checker = new UpdateSetFieldChecker();
        caseData.keySet().stream().forEach(s -> {

            Searcher searcher = mock(Searcher.class);
            Pack pack = caseData.get(s);
            pack.searchResult.keySet().stream().forEach(e -> {
                when(searcher.searchFieldRule(auth, e)).thenReturn(pack.searchResult.get(e));
            });

            DefaultContext context = new DefaultContext(sqlParser.parser(s), auth, searcher);
            checker.check(context);

            Assert.assertEquals(s, pack.expectation, context.isRefused());

        });

    }

    private Map<String, Pack> buildCase() {
        Map<String, Pack> data = new LinkedHashMap<>();

        data.put("update t1 set t1.c1=10",
            new Pack(
                false,
                new HashMap<String, List<FieldRule>>() {{
                    put("t1", Arrays.asList(
                        new FieldRule("t1", "c1"),
                        new FieldRule("t1", "c2")
                    ));
                }}
            )
        );

        data.put("update t1 set t1.c1=10",
            new Pack(
                true,
                new HashMap<String, List<FieldRule>>() {{
                    put("t2", Arrays.asList(
                        new FieldRule("t2", "c1"),
                        new FieldRule("t2", "c2")
                    ));
                }}
            )
        );

        data.put("update t1 set t1.c1='200',t1.c2=30",
            new Pack(
                false,
                new HashMap<String, List<FieldRule>>() {{
                    put("t1", Arrays.asList(
                        new FieldRule("t1", "c1"),
                        new FieldRule("t1", "c2")
                    ));
                }}
            )
        );


        return data;
    }

    private static class Pack {
        private boolean expectation;
        private Map<String, List<FieldRule>> searchResult;

        public Pack(boolean expectation, Map<String, List<FieldRule>> searchResult) {
            this.expectation = expectation;
            this.searchResult = searchResult;
        }
    }
}
