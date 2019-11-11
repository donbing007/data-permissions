package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.*;
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
public class JSqlParserFromAbilityTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testIterFrom() throws Exception {

        Map<String, List<From>> data = buildCase();
        Set<String> sqls = data.keySet();
        JSqlParserFromAbility handler;
        for (String sql : sqls) {
            handler = new JSqlParserFromAbility(CCJSqlParserUtil.parse(sql));
            List<From> froms = handler.list();
            List<From> expected = data.get(sql);

            Assert.assertEquals(sql, expected.size(), froms.size());

            if (expected.isEmpty()) {
                continue;
            }

            Iterator<From> fromIterator = froms.iterator();

            int index = 0;
            while (fromIterator.hasNext()) {
                Assert.assertEquals(sql, expected.get(index++), fromIterator.next());
            }
        }
    }

    private Map<String, List<From>> buildCase() throws Exception {

        Map<String, List<From>> data = new HashMap();
        data.put("select * from t1",
            Arrays.asList(
                new From("t1")
            ));

        data.put("select * from t1 t",
            Arrays.asList(
                new From("t1", new Alias("t"))
            ));

        data.put("select * from t1 a1 inner join t2 a2",
            Arrays.asList(
                new From("t1", new Alias("a1")),
                new From("t2", new Alias("a2"))
            ));

        data.put("select * from (select * from t1) t",
            Arrays.asList(
                new From(CCJSqlParserUtil.parse("select * from t1").toString(), new Alias("t"), true)
            ));

        data.put("select * from (select * from t2) t2 inner join t1 on t1.id=t2.id",
            Arrays.asList(
                new From(CCJSqlParserUtil.parse("select * from t2").toString(), new Alias("t2"), true),
                new From("t1")
            )
        );

        data.put("select * from t1 union select * from t2",
            Arrays.asList(
            )
        );

        data.put("select * from t1 inner join (select * from t2) t2 on t2.id=t1.id",
            Arrays.asList(
                new From("t1"),
                new From(CCJSqlParserUtil.parse("select * from t2").toString(), new Alias("t2"), true)
            )
        );

        data.put("insert into t (c1,c2,c3) values(1,2,3)",
            Arrays.asList(
                new From("t")
            )
        );

        data.put("update t set c1=2 where c1=1",
            Arrays.asList(
                new From("t")
            )
        );

        data.put("delete from t where c1=1",
            Arrays.asList(
                new From("t")
            )
        );

        return data;
    }
} 
