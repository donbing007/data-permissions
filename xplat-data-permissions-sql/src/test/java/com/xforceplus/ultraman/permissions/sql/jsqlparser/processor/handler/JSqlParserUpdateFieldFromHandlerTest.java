package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.From;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * JSqlParserUpdateFieldFromHandler Tester.
 *
 * @author <Authors name>
 * @version 1.0 11/04/2019
 * @since <pre>Nov 4, 2019</pre>
 */
public class JSqlParserUpdateFieldFromHandlerTest {

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

                JSqlParserUpdateFieldFromHandler h = new JSqlParserUpdateFieldFromHandler(CCJSqlParserUtil.parse(sql));
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

        data.put("update t set c1=10",
            new SearchPack(
                new Field("c1"),
                Arrays.asList(
                    new AbstractMap.SimpleEntry(new Field("c1"), new From("t"))
                ))
        );

        data.put("update t set t.c1=10",
            new SearchPack(
            new Field("t","c1",null),
            Arrays.asList(
                new AbstractMap.SimpleEntry(new Field("t", "c1"), new From("t"))
            ))
        );

        data.put("update t inner join t2 on t2.id=t.id set t.c1=t2.c2",
            new SearchPack(
                new Field("t2","c2",null),
                Arrays.asList(
                    new AbstractMap.SimpleEntry(new Field("t2", "c2"), new From("t2"))
                ))
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
