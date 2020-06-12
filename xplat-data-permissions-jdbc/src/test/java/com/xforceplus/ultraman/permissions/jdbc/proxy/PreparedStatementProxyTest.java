package com.xforceplus.ultraman.permissions.jdbc.proxy;

import com.mockrunner.mock.jdbc.MockResultSet;
import com.xforceplus.ultraman.permissions.jdbc.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.pojo.result.CheckStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.sql.hint.parser.SQL92HintParser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * PreparedStatementProxy Tester.
 *
 * @author <Authors name>
 * @version 1.0 11/28/2019
 * @since <pre>Nov 28, 2019</pre>
 */
public class PreparedStatementProxyTest {

    private Authorizations authorizations = new Authorizations(Arrays.asList(new Authorization("r1", "t1")));

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }


    @Test
    public void testExecuteQuery() throws Throwable {
        String sql = "select t.id from table t";

        RuleCheckServiceClient client = mock(RuleCheckServiceClient.class);
        when(client.check(sql, authorizations)).thenReturn(new CheckResult(CheckStatus.PASS));

        MockResultSet rs = new MockResultSet("rs");
        rs.addColumn("c1", new Integer[]{100});

        PreparedStatementProxy proxy = new PreparedStatementProxy(
            client,
            authorizations,
            innerSql -> {
                PreparedStatement preparedStatement = mock(PreparedStatement.class);
                when(preparedStatement.executeQuery()).thenReturn(rs);
                return preparedStatement;
            },
            new SQL92HintParser(),
            sql
        );

        Method method = PreparedStatement.class.getMethod("executeQuery", new Class[0]);
        ResultSet resultSet = (ResultSet) proxy.invoke(null, method, new Object[0]);

        resultSet.next();
        Assert.assertEquals(100, resultSet.getInt(1));
    }

    @Test
    public void testExecuteQueryDenial() throws Throwable {
        String sql = "select t.id from table t";

        RuleCheckServiceClient client = mock(RuleCheckServiceClient.class);
        when(client.check(sql, authorizations)).thenReturn(new CheckResult(CheckStatus.DENIAL));

        MockResultSet rs = new MockResultSet("rs");
        rs.addColumn("c1", new Integer[]{100});

        PreparedStatementProxy proxy = new PreparedStatementProxy(
            client,
            authorizations,
            innerSql -> {
                PreparedStatement preparedStatement = mock(PreparedStatement.class);
                when(preparedStatement.executeQuery()).thenReturn(rs);
                return preparedStatement;
            },
            new SQL92HintParser(),
            sql
        );

        Method method = PreparedStatement.class.getMethod("executeQuery", new Class[0]);
        ResultSet resultSet = (ResultSet) proxy.invoke(null, method, new Object[0]);

        Assert.assertFalse(resultSet.next());
    }

    @Test
    public void testExecuteQueryIgnore() throws Throwable {
        String sql = "select t.id from table t /* XDP:HINT ignore=true */";

        RuleCheckServiceClient client = mock(RuleCheckServiceClient.class);
        when(client.check(sql, authorizations)).thenReturn(new CheckResult(CheckStatus.PASS));

        MockResultSet rs = new MockResultSet("rs");
        rs.addColumn("c1", new Integer[]{100});

        PreparedStatementProxy proxy = new PreparedStatementProxy(
            client,
            authorizations,
            innerSql -> {
                PreparedStatement preparedStatement = mock(PreparedStatement.class);
                when(preparedStatement.executeQuery()).thenReturn(rs);
                return preparedStatement;
            },
            new SQL92HintParser(),
            sql
        );

        Method method = PreparedStatement.class.getMethod("executeQuery", new Class[0]);
        ResultSet resultSet = (ResultSet) proxy.invoke(null, method, new Object[0]);

        resultSet.next();
        Assert.assertEquals(100, resultSet.getInt(1));
    }

} 
