package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Alias;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.Func;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.values.LongValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.*;

/**
 * JSqlParserSelectItemHandler Tester.
 *
 * @author <Authors name>
 * @version 1.0 10/29/2019
 * @since <pre>Oct 29, 2019</pre>
 */
public class JSqlParserSelectItemAbilityTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: removeColumn(Field field)
     */
    @Test
    public void testRemoveColumn() throws Exception {
        Map<String, RemovePack> caseData = buildRemoveCase();

        Set<String> sqls = caseData.keySet();
        JSqlParserSelectItemAbility handler;
        RemovePack pack;
        Statement statement;
        for (String sql : sqls) {
             statement = CCJSqlParserUtil.parse(sql);
            handler = new JSqlParserSelectItemAbility(statement);

            pack = caseData.get(sql);

            if (Func.class.isInstance(pack.removeItem)) {
                handler.remove((Func) pack.removeItem);
            } else if (Field.class.isInstance(pack.removeItem)) {
                handler.remove((Field) pack.removeItem);
            }

            Assert.assertEquals(pack.expectedSql, statement.toString());
        }
    }

    private Map<String, RemovePack> buildRemoveCase() throws Exception {
        Map<String, RemovePack> data = new LinkedHashMap<>();

        data.put("select a,b,c from t1",
            new RemovePack(new Field("a"), CCJSqlParserUtil.parse("select b,c from t1").toString())
        );

        data.put("select c1 as a1, sum(t.c2) as sum from t1 t",
            new RemovePack(
                new Func("sum",
                    Arrays.asList(new Field("t", "c2", null)), new Alias("sum", true)),
                CCJSqlParserUtil.parse("select c1 as a1 from t1 t").toString())
        );

        return data;
    }

    private static class RemovePack {
        private Item removeItem;
        private String expectedSql;

        public RemovePack(Item removeItem, String expectedSql) {
            this.removeItem = removeItem;
            this.expectedSql = expectedSql;
        }
    }

    /**
     * Method: iterColumn()
     */
    @Test
    public void testIterColumn() throws Exception {
        Map<String, List<Item>> caseData = buildIterCase();
        Set<String> sqls = caseData.keySet();
        sqls.stream().forEach(sql -> {

            try {
                JSqlParserSelectItemAbility handler = new JSqlParserSelectItemAbility(CCJSqlParserUtil.parse(sql));
                List<Item> items = handler.list();
                List<Item> expectedItems = caseData.get(sql);
                Assert.assertEquals(sql, expectedItems.size(), items.size());
                for (int i = 0; i < expectedItems.size(); i++) {
                    Assert.assertEquals(sql, expectedItems.get(i), items.get(i));
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(),ex);
            }

        });


    }

    private Map<String, List<Item>> buildIterCase() {
        Map<String, List<Item>> data = new LinkedHashMap<>();

        data.put("select c1,c2 from t1",
            Arrays.asList(
                new Field("c1"),
                new Field("c2")
            )
        );

        data.put("select 1",
            Arrays.asList(
                new LongValue(1)
            )
        );

        data.put("select c1,count(*), max(c1) as top, current_time() from t1",
            Arrays.asList(
                new Field("c1"),
                new Func("count", Arrays.asList(new Field("*"))),
                new Func("max",Arrays.asList(new Field("c1")),new Alias("top", true)),
                new Func("current_time")
            )
        );

        data.put("select c1,c2 from t1 union select c3,c4 from t2",
            Arrays.asList(
                new Field("c1"),
                new Field("c2")
            )
        );

        data.put("select c1,c2 from t1 where exists (select c3,c4 from t2)",
            Arrays.asList(
                new Field("c1"),
                new Field("c2")
            )
        );

        return data;
    }


} 
