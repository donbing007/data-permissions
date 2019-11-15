package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Alias;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.From;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * JSqlParserDeleteFieldFromHandler Tester.
 *
 * @author <Authors name>
 * @version 1.0 11/04/2019
 * @since <pre>Nov 4, 2019</pre>
 */
public class JSqlParserDeleteFieldFromAbilityTest {

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

                JSqlParserDeleteFieldFromAbility h = new JSqlParserDeleteFieldFromAbility(CCJSqlParserUtil.parse(sql));
                SearchPack pack = caseData.get(sql);
                List<AbstractMap.SimpleEntry<Field, From>> froms = h.searchRealTableName(pack.field);

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
        data.put("delete from t1 where id=2",
            new SearchPack(
                new Field("id"),
                Arrays.asList(
                    new AbstractMap.SimpleEntry(new Field("id"), new From("t1"))
                )
            )
        );

        data.put("delete from t1 t where t.id=2 or t.id=3",
            new SearchPack(
                new Field("t", "id"),
                Arrays.asList(
                    new AbstractMap.SimpleEntry(new Field("t", "id"), new From("t1", new Alias("t")))
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
