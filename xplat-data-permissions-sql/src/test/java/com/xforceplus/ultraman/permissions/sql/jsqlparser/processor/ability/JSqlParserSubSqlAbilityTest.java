package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.JSql;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitorAdapter;
import com.xforceplus.ultraman.permissions.sql.processor.SubSelectSqlProcessor;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.*;

/**
 * JSqlParserSubSqlHandler Tester.
 *
 * @author <Authors name>
 * @version 1.0 10/31/2019
 * @since <pre>Oct 31, 2019</pre>
 */
public class JSqlParserSubSqlAbilityTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: list()
     */
    @Test
    public void testList() throws Exception {

        Map<String, List<String>> caseData = buildListCase();
        Set<String> sqls = caseData.keySet();
        sqls.stream().forEach(sql -> {

            try {

                Sql root = new JSql(CCJSqlParserUtil.parse(sql));
                Queue<Sql> queue = new ArrayDeque<>();
                queue.add(root);

                List<String> current = new ArrayList<>();
                while(!queue.isEmpty()) {

                    Sql s = queue.poll();
                    current.add(s.toSqlString());

                    s.visit(new SqlProcessorVisitorAdapter() {
                        @Override
                        public void visit(SelectSqlProcessor processor) {
                            queue.addAll(processor.buildSubSqlAbility().list());
                        }

                        @Override
                        public void visit(SubSelectSqlProcessor processor) {
                            queue.addAll(processor.buildSubSqlAbility().list());
                        }
                    });
                }

                current.remove(0);// 删除首条.



                List<String> expectedSqls = caseData.get(sql);
                Assert.assertEquals(expectedSqls.size(), current.size());
                for (int i = 0; i < expectedSqls.size(); i++) {
                    Assert.assertEquals(sql, expectedSqls.get(i), current.get(i));
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }

        });

    }

    private Map<String, List<String>> buildListCase() throws Exception {
        Map<String, List<String>> data = new LinkedHashMap<>();

        data.put("select * from t1",
            Arrays.asList()
        );

        data.put("select * from (select * from t1) t",
            Arrays.asList(
                CCJSqlParserUtil.parse("select * from t1").toString()
            )
        );

        data.put("select * from (select * from t1) t union select * from t2",
            Arrays.asList(
                CCJSqlParserUtil.parse("select * from (select * from t1) t").toString(),
                CCJSqlParserUtil.parse("select * from t2").toString(),
                CCJSqlParserUtil.parse("select * from t1").toString()
            )
        );

        data.put("select * from (select * from t1) t union select * from t2 union all select * from t3",
            Arrays.asList(
                CCJSqlParserUtil.parse("select * from (select * from t1) t").toString(),
                CCJSqlParserUtil.parse("select * from t2").toString(),
                CCJSqlParserUtil.parse("select * from t3").toString(),
                CCJSqlParserUtil.parse("select * from t1").toString()
            )
        );

        data.put("select * from (select * from (select * from t1)) t",
            Arrays.asList(
                CCJSqlParserUtil.parse("select * from (select * from t1)").toString(),
                CCJSqlParserUtil.parse("select * from t1").toString()
            )
        );

        return data;
    }
} 
