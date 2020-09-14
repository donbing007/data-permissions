package com.xforceplus.ultraman.permissions.jdbc.proxy;

import com.mockrunner.mock.jdbc.MockResultSet;
import com.xforceplus.ultraman.permissions.jdbc.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.jdbc.parser.VariableParserManager;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.pojo.check.SqlChange;
import com.xforceplus.ultraman.permissions.pojo.result.CheckStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.sql.hint.parser.SQL92HintParser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * StatementProxy Tester.
 *
 * @author <Authors name>
 * @version 1.0 06/11/2020
 * @since <pre>Jun 11, 2020</pre>
 */
public class StatementProxyTest {

    private Authorizations authorizations = new Authorizations(Arrays.asList(new Authorization("r1", "t1")));

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }


    @Test
    public void testExecute() throws Throwable {

        String sql = "update set c1=100 from table";

        RuleCheckServiceClient client = mock(RuleCheckServiceClient.class);
        when(client.check(sql, authorizations)).thenReturn(new CheckResult(CheckStatus.PASS));

        Statement statement = mock(Statement.class);
        when(statement.execute(sql)).thenReturn(true);

        StatementProxy proxy = new StatementProxy(client, authorizations, statement, new SQL92HintParser());
        Method method = Statement.class.getMethod("execute", new Class[]{String.class});
        Object result = proxy.invoke(null, method, new Object[]{sql});

        Assert.assertEquals(Boolean.class, result.getClass());

    }

    @Test
    public void testExecuteUpdate() throws Throwable {

        String sql = "update set c1=100 from table";

        RuleCheckServiceClient client = mock(RuleCheckServiceClient.class);
        when(client.check(sql, authorizations)).thenReturn(new CheckResult(CheckStatus.PASS));

        Statement statement = mock(Statement.class);
        when(statement.executeUpdate(sql)).thenReturn(100);

        StatementProxy proxy = new StatementProxy(client, authorizations, statement, new SQL92HintParser());
        Method method = Statement.class.getMethod("executeUpdate", new Class[]{String.class});
        Object result = proxy.invoke(null, method, new Object[]{sql});

        Assert.assertEquals(100, result);

    }

    @Test
    public void testExecuteResult() throws Throwable {

        String sql = "select t.id from table t";

        RuleCheckServiceClient client = mock(RuleCheckServiceClient.class);
        when(client.check(sql, authorizations)).thenReturn(new CheckResult(CheckStatus.PASS));

        MockResultSet rs = new MockResultSet("rs");
        rs.addColumn("c1", new Integer[]{100});

        Statement statement = mock(Statement.class);
        when(statement.executeQuery(sql)).thenReturn(rs);

        StatementProxy proxy = new StatementProxy(client, authorizations, statement, new SQL92HintParser());
        Method method = Statement.class.getMethod("executeQuery", new Class[]{String.class});
        ResultSet resultSet = (ResultSet) proxy.invoke(null, method, new Object[]{sql});

        resultSet.next();
        Assert.assertEquals(100, resultSet.getInt(1));

    }

    /**
     * 没有权限,但是因为忽略了权限检查所以应该可以得到结果.
     *
     * @throws Throwable
     */
    @Test
    public void testExecuteResultIgnore() throws Throwable {

        String sql = "select t.id from table t /* XDP:HINT ignore=true */";

        RuleCheckServiceClient client = mock(RuleCheckServiceClient.class);
        when(client.check(sql, authorizations)).thenReturn(new CheckResult(CheckStatus.DENIAL));

        MockResultSet rs = new MockResultSet("rs");
        rs.addColumn("c1", new Integer[]{100});

        Statement statement = mock(Statement.class);
        when(statement.executeQuery(sql)).thenReturn(rs);

        StatementProxy proxy = new StatementProxy(client, authorizations, statement, new SQL92HintParser());
        Method method = Statement.class.getMethod("executeQuery", new Class[]{String.class});
        ResultSet resultSet = (ResultSet) proxy.invoke(null, method, new Object[]{sql});

        resultSet.next();
        Assert.assertEquals(100, resultSet.getInt(1));

    }


    @Test
    public void testExecuteResultDenial() throws Throwable {

        String sql = "select t.id from table t";

        RuleCheckServiceClient client = mock(RuleCheckServiceClient.class);
        when(client.check(sql, authorizations)).thenReturn(new CheckResult(CheckStatus.DENIAL));

        MockResultSet rs = new MockResultSet("rs");
        rs.addColumn("c1", new Integer[]{100});

        Statement statement = mock(Statement.class);
        when(statement.executeQuery(sql)).thenReturn(rs);
        when(statement.getResultSet()).thenReturn(rs);

        StatementProxy proxy = new StatementProxy(client, authorizations, statement, new SQL92HintParser());
        Method method = Statement.class.getMethod("executeQuery", new Class[]{String.class});
        ResultSet resultSet = (ResultSet) proxy.invoke(null, method, new Object[]{sql});
        Assert.assertFalse(resultSet.next());

        method = Statement.class.getMethod("execute", new Class[]{String.class});
        Assert.assertFalse((Boolean) proxy.invoke(null, method, new Object[]{sql}));
    }

    @Test
    public void testExecuteResultUpdate() throws Throwable {

        String sql = "select t.id from table t";
        String newSql = "select t.id from table t where t.id > 200";

        VariableParserManager manager = mock(VariableParserManager.class);
        when(manager.parse(newSql)).thenReturn(newSql);

        RuleCheckServiceClient client = mock(RuleCheckServiceClient.class);
        when(client.check(sql, authorizations)).thenReturn(new CheckResult(CheckStatus.UPDATE, new SqlChange(newSql)));

        MockResultSet rs = new MockResultSet("rs");
        rs.addColumn("c1", new Integer[]{100});

        Statement statement = mock(Statement.class);
        when(statement.executeQuery(newSql)).thenReturn(rs);

        StatementProxy proxy = new StatementProxy(client, authorizations, statement, new SQL92HintParser(),manager);
        Method method = Statement.class.getMethod("executeQuery", new Class[]{String.class});
        ResultSet resultSet = (ResultSet) proxy.invoke(null, method, new Object[]{sql});

        resultSet.next();
        Assert.assertEquals(100, resultSet.getInt(1));

        verify(statement, times(1)).executeQuery(newSql);
    }


}
