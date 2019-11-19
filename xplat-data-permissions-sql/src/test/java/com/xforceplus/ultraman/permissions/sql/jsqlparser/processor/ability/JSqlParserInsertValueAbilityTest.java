package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Func;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.Arithmeitc;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.ArithmeticSymbol;
import com.xforceplus.ultraman.permissions.sql.define.values.JdbcParameterValue;
import com.xforceplus.ultraman.permissions.sql.define.values.LongValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.*;

/**
 * JSqlParserInsertValueHandler Tester.
 *
 * @author <Authors name>
 * @version 1.0 10/30/2019
 * @since <pre>Oct 30, 2019</pre>
 */
public class JSqlParserInsertValueAbilityTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: list(int index)
     */
    @Test
    public void testList() throws Exception {

        Map<String, ListPack> caseData = buildListCase();
        Set<String> sqls = caseData.keySet();
        sqls.stream().forEach(sql -> {
            try {

                JSqlParserInsertValueAbility h = new JSqlParserInsertValueAbility(CCJSqlParserUtil.parse(sql));
                ListPack pack = caseData.get(sql);
                List<Item> items = h.list(pack.point);

                Assert.assertEquals(sql, pack.items.size(), items.size());

                for (int i = 0; i < pack.items.size(); i++) {
                    Assert.assertEquals(sql, pack.items.get(i), items.get(i));
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        });
    }

    private Map<String, ListPack> buildListCase() {
        Map<String, ListPack> data = new LinkedHashMap<>();

        data.put("insert t1 (c1) value(1)",
            new ListPack(
                0,
                Arrays.asList(
                    new LongValue(1)
                )
            ));

        data.put("insert t1 (c1,c2) value(1,current_time())",
            new ListPack(
                0,
                Arrays.asList(
                    new LongValue(1),
                    new Func("current_time")
                )
            ));

        data.put("insert t1 (c1,c2) values(1,current_time()),(2,current_time())",
            new ListPack(
                1,
                Arrays.asList(
                    new LongValue(2),
                    new Func("current_time")
                )
            ));

        data.put("insert t1 (c1,c2) values(1,1+2)",
            new ListPack(
                0,
                Arrays.asList(
                    new LongValue(1),
                    new Arithmeitc(new LongValue(1), new LongValue(2), ArithmeticSymbol.ADDITION)
                )
            ));

        data.put("insert t1 (c1,c2) values(?,?)",
            new ListPack(
                0,
                Arrays.asList(
                    JdbcParameterValue.geInstance(),
                    JdbcParameterValue.geInstance()
                )
            ));

        return data;
    }

    private static class ListPack {
        private int point;
        private List<Item> items;

        public ListPack(int point, List<Item> items) {
            this.point = point;
            this.items = items;
        }
    }

    /**
     * Method: size()
     */
    @Test
    public void testSize() throws Exception {

        Map<String, Integer> caseData = buildSizeCase();
        Set<String> sqls = caseData.keySet();
        sqls.stream().forEach(sql -> {
            try {

                JSqlParserInsertValueAbility h = new JSqlParserInsertValueAbility(CCJSqlParserUtil.parse(sql));
                Assert.assertEquals(caseData.get(sql).intValue(), h.size());

            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        });

    }

    private Map<String, Integer> buildSizeCase() {
        Map<String, Integer> data = new LinkedHashMap<>();

        data.put("insert t1 (c1) value(1)", 1);
        data.put("insert t1 (c1,c2) values(1,2),(3,4)", 2);

        return data;
    }

} 
