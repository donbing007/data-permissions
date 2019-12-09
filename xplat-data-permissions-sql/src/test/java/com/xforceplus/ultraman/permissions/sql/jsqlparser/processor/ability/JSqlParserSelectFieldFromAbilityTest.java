package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.*;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.Arithmeitc;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.ArithmeticSymbol;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.*;

/**
 * JSqlParserFieldFromHandler Tester.
 *
 * @author <Authors name>
 * @version 1.0 11/01/2019
 * @since <pre>Nov 1, 2019</pre>
 */
public class JSqlParserSelectFieldFromAbilityTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: searchRealTableName(Field field)
     */
    @Test
    public void testSearchRealTableName() throws Exception {
        Map<String, SearchPack> caseData = buildSearchCase();
        Set<String> sqls = caseData.keySet();
        sqls.stream().forEach(sql -> {
            try {

                JSqlParserSelectFieldFromAbility h = new JSqlParserSelectFieldFromAbility(CCJSqlParserUtil.parse(sql));
                SearchPack pack = caseData.get(sql);
                List<AbstractMap.SimpleEntry<Field, From>> froms = h.searchRealTableName(pack.item);

                Assert.assertEquals(sql, pack.expectedFroms.size(), froms.size());
                Assert.assertArrayEquals(sql, pack.expectedFroms.toArray(new AbstractMap.SimpleEntry[0]),
                    froms.toArray(new AbstractMap.SimpleEntry[0]));

            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        });
    }

    private Map<String, SearchPack> buildSearchCase() {
        Map<String, SearchPack> data = new LinkedHashMap<>();
        data.put("select t.id from t1 t",
            new SearchPack(new Field("t", "id", null),
                Arrays.asList(
                    new AbstractMap.SimpleEntry(
                        new Field("t", "id", null),
                        new From("t1", new Alias("t", false), false))
                ))
        );

        data.put("select ta1.id from (" +
                "select ta2.id from (" +
                "select t1.id from t1" +
                ") ta2" +
                ") ta1",
            new SearchPack(new Field("ta1", "id", null),
                Arrays.asList(
                    new AbstractMap.SimpleEntry(new Field("t1", "id", null), new From("t1"))
                ))
        );

        data.put("select func(t.id) from t",
            new SearchPack(new Field("t", "id", null),
                Arrays.asList(
                    new AbstractMap.SimpleEntry(new Field("t", "id", null), new From("t"))
                ))
        );

        data.put("select ta1.total from (" +
                "select func(t1.id,t2.id) total from t1 inner join t2 on t1.id=t2.id" +
                ") ta1",
            new SearchPack(new Field("ta1", "total", null),
                Arrays.asList(
                    new AbstractMap.SimpleEntry(new Field("t1", "id", null), new From("t1")),
                    new AbstractMap.SimpleEntry(new Field("t2", "id", null), new From("t2"))
                )
            )
        );

        data.put("select t.num from (select t1.c1+t1.c2 num from t1) t",
            new SearchPack(new Field("t", "num"),
                Arrays.asList(
                    new AbstractMap.SimpleEntry(new Field("t1", "c1", null), new From("t1")),
                    new AbstractMap.SimpleEntry(new Field("t1", "c2", null), new From("t1"))
                )
            )
        );

        data.put("select t.num from (select (t1.c1+t1.c2) num from t1) t",
            new SearchPack(new Field("t", "num"),
                Arrays.asList(
                    new AbstractMap.SimpleEntry(new Field("t1", "c1", null), new From("t1")),
                    new AbstractMap.SimpleEntry(new Field("t1", "c2", null), new From("t1"))
                )
            )
        );


        data.put("select (t.num + t2.c2) r from (select t1.id, t1.c1+t1.c2 num from t1) t inner join t2 on t2.id=t.id",
            new SearchPack(
                new Parentheses(
                    new Arithmeitc(
                        new Field("t", "num"),
                        new Field("t2", "c2"),
                        ArithmeticSymbol.ADDITION
                    ),
                    new Alias("r")
                ),
                Arrays.asList(
                    new AbstractMap.SimpleEntry(new Field("t1", "c1"), new From("t1")),
                    new AbstractMap.SimpleEntry(new Field("t1", "c2"), new From("t1")),
                    new AbstractMap.SimpleEntry(new Field("t2", "c2"), new From("t2"))
                )
            )
        );

        data.put("select distinct summ.auth_tax_period as auth_tax_period from crp_purchaser_auth_summ summ " +
                "where summ.auth_tax_period is not null and summ.auth_tax_period <> '' " +
                "union all select s.name as auth_tax_period from dim_auth_charge_status s where s.type = 'noAuth'",
            new SearchPack(
                new Field("summ", "auth_tax_period", new Alias("auth_tax_period", true)),
                Arrays.asList(
                    new AbstractMap.SimpleEntry(
                        new Field("summ", "auth_tax_period", new Alias("auth_tax_period", true)),
                        new From("crp_purchaser_auth_summ",new Alias("summ")))
                )
            )
        );


        return data;
    }

    private static class SearchPack {
        private Item item;
        private List<AbstractMap.SimpleEntry<Field, From>> expectedFroms;

        public SearchPack(Item item, List<AbstractMap.SimpleEntry<Field, From>> expectedFroms) {
            this.item = item;
            this.expectedFroms = expectedFroms;
        }
    }

} 
