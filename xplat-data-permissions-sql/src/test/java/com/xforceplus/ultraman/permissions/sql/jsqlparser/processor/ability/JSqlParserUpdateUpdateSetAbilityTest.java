package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.Func;
import com.xforceplus.ultraman.permissions.sql.define.UpdateSet;
import com.xforceplus.ultraman.permissions.sql.define.values.LongValue;
import com.xforceplus.ultraman.permissions.sql.define.values.StringValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * JSqlParserUpdateSetHandler Tester.
 *
 * @author <Authors name>
 * @version 1.0 10/30/2019
 * @since <pre>Oct 30, 2019</pre>
 */
public class JSqlParserUpdateUpdateSetAbilityTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: iterator()
     */
    @Test
    public void testIterator() throws Exception {
        Map<String, List<UpdateSet>> caseData = buildIterCase();
        Set<String> sqls = caseData.keySet();
        sqls.stream().forEach(sql -> {
            try {
                Statement statement = CCJSqlParserUtil.parse(sql);
                JSqlParserUpdateSetAbility h = new JSqlParserUpdateSetAbility(statement);

                List<UpdateSet> current = h.list();

                List<UpdateSet> expectedList = caseData.get(sql);
                Assert.assertEquals(expectedList.size(), current.size());
                for (int i=0; i < expectedList.size(); i++) {

                    Assert.assertEquals(expectedList.get(i).toSqlString(), current.get(i).toSqlString());
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        });

    }

    public Map<String, List<UpdateSet>> buildIterCase() {
        Map<String, List<UpdateSet>> data = new LinkedHashMap<>();
        data.put("update t1 set c1=2 where id=1",
            Arrays.asList(
                new UpdateSet(new Field("c1"), new LongValue(2))
            )
        );

        data.put("update t1 set c1=2,c2='value' where id=1",
            Arrays.asList(
                new UpdateSet(new Field("c1"), new LongValue(2)),
                new UpdateSet(new Field("c2"), new StringValue("value"))
            )
        );

        data.put("update t1 set c1=2,c2='value',c3=BIT_LENGHT('text') where id=1",
            Arrays.asList(
                new UpdateSet(new Field("c1"), new LongValue(2)),
                new UpdateSet(new Field("c2"), new StringValue("value")),
                new UpdateSet(new Field("c3"), new Func("BIT_LENGHT", Arrays.asList(new StringValue("text"))))
            )
        );

        return data;
    }


} 
