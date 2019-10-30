package com.xforceplus.ultraman.permissions.sql.define;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * Func Tester.
 *
 * @author <Authors name>
 * @version 1.0 10/30/2019
 * @since <pre>Oct 30, 2019</pre>
 */
public class FuncTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testFixName() throws Exception {
        String name = "current_time()";
        Func func = new Func(name);
        Assert.assertEquals("current_time", func.getName());

        name = "sum(1,2,3)";
        func = new Func(name);
        Assert.assertEquals("sum", func.getName());
    }

} 
