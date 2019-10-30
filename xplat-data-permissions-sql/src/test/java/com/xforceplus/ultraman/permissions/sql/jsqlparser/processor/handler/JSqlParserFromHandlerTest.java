package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler;

import com.xforceplus.ultraman.permissions.sql.define.From;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.*;

/**
 * JSqlParserFromHandler Tester.
 *
 * @author <Authors name>
 * @version 1.0 10/28/2019
 * @since <pre>Oct 28, 2019</pre>
 */
public class JSqlParserFromHandlerTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testIterFrom() throws Exception {

        Map<String,List<From>> data = buildCase();
        Set<String> sqls = data.keySet();
        JSqlParserFromHandler handler;
        for (String sql : sqls) {
            handler = new JSqlParserFromHandler(CCJSqlParserUtil.parse(sql));
            Iterator<From> fromIterator = handler.list().iterator();

            List<From> expected = data.get(sql);
            int index = 0;
            while(fromIterator.hasNext()) {
                Assert.assertEquals(expected.get(index++), fromIterator.next());
            }
        }
    }

    private Map<String, List<From>> buildCase() {

        Map<String,List<From>> data = new HashMap();
        data.put("select * from t1",
            Arrays.asList(
                new From("t1")
            ));

        data.put("select * from t1 t",
            Arrays.asList(
                new From("t1", "t")
            ));

        data.put("select * from t1 a1 inner join t2 a2",
            Arrays.asList(
                new From("t1", "a1"),
                new From("t2", "a2")
            ));

        data.put("select * from (select * from t1) t",
            Arrays.asList(
                new From("", "t", true)
            ));

        return data;
    }
} 
