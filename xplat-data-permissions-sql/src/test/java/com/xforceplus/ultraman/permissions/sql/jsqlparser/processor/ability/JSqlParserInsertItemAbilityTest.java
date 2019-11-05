package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.*;

/**
 * JSqlParserInsertItemHandler Tester.
 *
 * @author <Authors name>
 * @version 1.0 10/30/2019
 * @since <pre>Oct 30, 2019</pre>
 */
public class JSqlParserInsertItemAbilityTest {

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
        Map<String, List<Field>> caseData = buildIterCase();
        Set<String> sqls = caseData.keySet();
        sqls.stream().forEach(sql -> {
            try {
                JSqlParserInsertItemAbility h = new JSqlParserInsertItemAbility(CCJSqlParserUtil.parse(sql));

                List<Field> current = h.list();

            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        });

    }

    private Map<String, List<Field>> buildIterCase() {
        Map<String, List<Field>> data = new LinkedHashMap<>();
        data.put("insert into t1 (c1,c2,c3) values(1,2,3)",
            Arrays.asList(
                new Field("c1"),
                new Field("c2"),
                new Field("c3")
            ));

        return data;
    }
} 
