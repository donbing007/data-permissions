package com.xforceplus.ultraman.permissions.sql.jsqlparser;

import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.sql.SQLType;

/**
 * JSql Tester.
 *
 * @author <Authors name>
 * @version 1.0 11/01/2019
 * @since <pre>Nov 1, 2019</pre>
 */
public class JSqlTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: type()
     */
    @Test
    public void testType() throws Exception {
        Statement statement = CCJSqlParserUtil.parse( "select * from t1 union select * from t2");
        JSql sql = new JSql(statement);
        Assert.assertEquals(SqlType.SELECT, sql.type());

        statement = CCJSqlParserUtil.parse( "delete from t1 where id=2");
        sql = new JSql(statement);
        Assert.assertEquals(SqlType.DELETE, sql.type());

        statement = CCJSqlParserUtil.parse( "update t1 set c1=1 where c2=10");
        sql = new JSql(statement);
        Assert.assertEquals(SqlType.UPDATE, sql.type());
    }

    /**
     * Method: isUnion()
     */
    @Test
    public void testIsUnion() throws Exception {

        Statement statement = CCJSqlParserUtil.parse( "select * from t1 union select * from t2");
        JSql sql = new JSql(statement);
        Assert.assertTrue(sql.isUnion());

        statement = CCJSqlParserUtil.parse( "select * from t1");
        sql = new JSql(statement);
        Assert.assertFalse(sql.isUnion());
    }
}
