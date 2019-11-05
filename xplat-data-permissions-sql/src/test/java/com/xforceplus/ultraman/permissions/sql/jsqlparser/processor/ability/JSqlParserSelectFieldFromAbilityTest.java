package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Alias;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.From;
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
                List<AbstractMap.SimpleEntry<Field, From>> froms = h.searchRealTableName(pack.field);

                Assert.assertEquals(sql, pack.expectedFroms.size(), froms.size());
                Assert.assertArrayEquals(sql, pack.expectedFroms.toArray(new AbstractMap.SimpleEntry[0]), froms.toArray(new AbstractMap.SimpleEntry[0]));

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
                    new AbstractMap.SimpleEntry(new Field("t","id", null),
                        new From("t1",new Alias("t", false), false))
            ))
        );

        data.put("select ta1.id from (" +
                    "select ta2.id from (" +
                        "select t1.id from t1" +
                    ") ta2" +
                ") ta1",
            new SearchPack(new Field("ta1","id", null),
                Arrays.asList(
                    new AbstractMap.SimpleEntry(new Field("t1","id", null), new From("t1"))
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
            new SearchPack(new Field("ta1","total", null),
                Arrays.asList(
                    new AbstractMap.SimpleEntry(new Field("t1","id", null),new From("t1")),
                    new AbstractMap.SimpleEntry(new Field("t2","id", null),new From("t2"))
                )
            )
        );

        return data;
    }

    private static class SearchPack {
        private Field field;
        private List<AbstractMap.SimpleEntry<Field, From>> expectedFroms;

        public SearchPack(Field field, List<AbstractMap.SimpleEntry<Field, From>> expectedFroms) {
            this.field = field;
            this.expectedFroms = expectedFroms;
        }
    }

} 
