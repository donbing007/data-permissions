package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.*;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.Arithmeitc;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.ArithmeticSymbol;
import com.xforceplus.ultraman.permissions.sql.define.values.LongValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
                throw new RuntimeException(ex.getMessage(), ex);
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
                new Func("max", Arrays.asList(new Field("c1")), new Alias("top", true)),
                new Func("current_time")
            )
        );

        data.put("select c1,c2 from t1 where exists (select c3,c4 from t2)",
            Arrays.asList(
                new Field("c1"),
                new Field("c2")
            )
        );

        data.put("select c1+c2 total from t1",
            Arrays.asList(
                new Arithmeitc(new Field("c1"), new Field("c2"), ArithmeticSymbol.ADDITION, new Alias("total"))
            )
        );

        data.put("select (c1+c2) total from t1",
            Arrays.asList(
                new Parentheses(
                    new Arithmeitc(new Field("c1"), new Field("c2"), ArithmeticSymbol.ADDITION),
                    new Alias("total"))
            )
        );

        data.put("select sum(t.c1, t.c2) as s from (select t1.c1,t2.c2,t.c3,t.count count from t1 ) t where t.count=00000",
            Arrays.asList(
                new Func(
                    "sum",
                    Arrays.asList(
                        new Field("t", "c1"),
                        new Field("t", "c2")
                    ),
                    new Alias("s", true)
                )
            )
        );

        data.put("select distinct summ.auth_tax_period as auth_tax_period from crp_purchaser_auth_summ summ " +
                "where summ.auth_tax_period is not null and summ.auth_tax_period <> '' " +
                "union all select s.name as auth_tax_period from dim_auth_charge_status s where s.type = 'noAuth'",
            Arrays.asList(
                new Field("summ", "auth_tax_period", new Alias("auth_tax_period", true))
            )
        );

        return data;
    }


} 
