package com.xforceplus.ultraman.permissions.rule.check.select;


import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.rule.context.DefaultContext;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.sql.SqlParser;
import com.xforceplus.ultraman.permissions.sql.define.Field;
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

    private Authorization auth = new Authorization("r1", "t1");
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
        Map<SearchRequest, List<RuleCheckPack>> caseData = buildCase();
        caseData.keySet().stream().forEach(request -> {
            try {

                Searcher searcher = mock(Searcher.class);
                List<RuleCheckPack> packs = caseData.get(request);
                for (RuleCheckPack pack : packs) {
                    when(searcher.searchFieldRule(pack.auth, pack.entity)).thenReturn(pack.rules);
                }


                Context context = new DefaultContext(sqlParser.parser(request.sql), auth, searcher);
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
        private Authorization auth;
        private String entity;
        private List<FieldRule> rules;

        public RuleCheckPack(Authorization auth, String entity, List<FieldRule> rules) {
            this.auth = auth;
            this.entity = entity;
            this.rules = rules;
        }
    }

    private Map<SearchRequest, List<RuleCheckPack>> buildCase() {
        Map<SearchRequest, List<RuleCheckPack>> data = new LinkedHashMap<>();

        data.put(new SearchRequest(
                "select t.c1, t.c2 from t1 t",
                false,
                Collections.emptyList()),
            Arrays.asList(
                new RuleCheckPack(auth, "t1",
                    Arrays.asList(
                        new FieldRule("t1", "c1"),
                        new FieldRule("t1", "c2"),
                        new FieldRule("t1", "c3")
                    )
                )
            )
        );

        data.put(new SearchRequest(
                "select t.c1, t.c3 from t1 t",
                false,
                Arrays.asList(
                    new Field("t", "c1")
                )),
            Arrays.asList(
                new RuleCheckPack(auth, "t1",
                    Arrays.asList(
                        new FieldRule("t1", "c3"),
                        new FieldRule("t1", "c4"),
                        new FieldRule("t1", "c5")
                    )
                )
            )
        );

        data.put(new SearchRequest(
                "select t.c1 from t1 t",
                true,
                Arrays.asList(
                    new Field("t", "c1")
                )),
            Arrays.asList(
                new RuleCheckPack(auth, "t1",
                    Arrays.asList(
                        new FieldRule("t1", "c3"),
                        new FieldRule("t1", "c4"),
                        new FieldRule("t1", "c5")
                    )
                )
            )
        );

        data.put(new SearchRequest(
                "select t.c1andc2 from t1 (select (t2.c1+t2.c2) c1andc2 from t2)",
                false,
                Arrays.asList(
                )),
            Arrays.asList(
                new RuleCheckPack(auth, "t2",
                    Arrays.asList(
                        new FieldRule("t2", "c1"),
                        new FieldRule("t2", "c2"),
                        new FieldRule("t2", "c3")
                    )
                )
            )
        );

        data.put(new SearchRequest(
                "select t.c1andc2 from (select (t2.c1+t2.c2) c1andc2 from t2) t",
                true,
                Arrays.asList(
                    new Field("t", "c1andc2")
                )),
            Arrays.asList(
                new RuleCheckPack(auth, "t2",
                    Arrays.asList(
                        new FieldRule("t2", "c2"),
                        new FieldRule("t2", "c3")
                    )
                )
            )
        );

        data.put(new SearchRequest(
                "select t1.c2 from t1 where t1.c1=10",
                true,
                Arrays.asList(
                )),
            Arrays.asList(
                new RuleCheckPack(auth, "t1",
                    Arrays.asList(
                        new FieldRule("t1", "c2"),
                        new FieldRule("t1", "c3")
                    )
                )
            )
        );

        data.put(new SearchRequest(
                "select t1.c2 from t1 where t1.c1=10",
                false,
                Arrays.asList(
                )),
            Arrays.asList(
                new RuleCheckPack(auth, "t1",
                    Arrays.asList(
                        new FieldRule("t1", "*")
                    )
                )
            )
        );

        data.put(new SearchRequest(
                "select t1.c2 from t1 where t1.c1=10",
                true,
                Arrays.asList(
                    new Field("t1", "c2")
                )),
            Arrays.asList(
                new RuleCheckPack(auth, "t1",
                    Arrays.asList(
                        new FieldRule("t1", "*"),
                        new FieldRule("t1", "c3")
                    )
                )
            )
        );

        return data;
    }
}
