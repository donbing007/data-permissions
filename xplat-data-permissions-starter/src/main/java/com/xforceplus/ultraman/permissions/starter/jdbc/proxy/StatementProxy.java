package com.xforceplus.ultraman.permissions.starter.jdbc.proxy;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.pojo.result.CheckStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.starter.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.starter.jdbc.proxy.resultset.DenialResultSet;
import com.xforceplus.ultraman.permissions.starter.jdbc.proxy.resultset.PassResultSetProxy;
import com.xforceplus.ultraman.permissions.starter.utils.MethodHelper;
import com.xforceplus.ultraman.permissions.starter.utils.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 处理 statement 类型.
 * 会关注以下两个方法.
 * ResultSet executeQuery(String sql) throws SQLException;
 * int executeUpdate(String sql) throws SQLException;
 * 拦截其方法的执行,在执行之前进行权限验证.
 *
 * @version 0.1 2019/11/15 17:11
 * @auth dongbin
 * @see java.sql.Statement
 * @since 1.8
 */
public class StatementProxy extends AbstractStatementProxy implements InvocationHandler {

    private static final Class[] METHOD_PARAMETER_TYPE = new Class[]{String.class};
    private Statement statement;

    public StatementProxy(RuleCheckServiceClient client, Authorizations authorizations, Statement statement) {
        super(client,authorizations);
        this.statement = statement;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (MethodHelper.isTarget(method, "executeQuery", METHOD_PARAMETER_TYPE, ResultSet.class)
            || MethodHelper.isTarget(method, "executeUpdate", METHOD_PARAMETER_TYPE, Integer.TYPE)
            || MethodHelper.isTarget(method, "execute", METHOD_PARAMETER_TYPE, Boolean.TYPE)) {
            return doExecute(method, args);
        } else {
            return method.invoke(statement, args);
        }
    }

    public Statement getStatement() {
        return statement;
    }

    private Object doExecute(Method method, Object[] args) throws Throwable {
        String sql = (String) args[0];

        return doCheck(sql, method, args);
    }

    protected Object doCheck(String sql, Method method, Object[] args) throws Throwable {
        CheckResult checkResult = getClient().check(sql, getAuthorization());
        CheckStatus status = checkResult.getStatus();

        switch (status) {
            case PASS: {

                ResultSet target = (ResultSet) method.invoke(statement, args);

                return ProxyFactory.createInterfaceProxy(
                    target,
                    new PassResultSetProxy(checkResult.findFirst().getBlackList(), target));
            }
            case UPDATE: {

                String newSql = checkResult.findFirst().getNewSql();
                if (newSql == null) {
                    throw new IllegalStateException("The status is updated, but no replacement SQL statement was found!");
                }

                ResultSet target = (ResultSet) method.invoke(statement, new Object[]{checkResult.findFirst().getNewSql()});
                return ProxyFactory.createInterfaceProxy(
                    target,
                    new PassResultSetProxy(checkResult.findFirst().getBlackList(), target));
            }
            case DENIAL: {
                if (Integer.TYPE.equals(method.getReturnType())) {
                    return 0;
                } else {
                    return DenialResultSet.getInstance();
                }
            }
            case ERROR: {
                String message = checkResult.getMessage();
                throw new SQLException(message != null ? message : "");
            }
            case NOT_SUPPORT: {
                throw new SQLException("Unsupported SQL statements.");
            }
            default: {
                throw new SQLException("Unknown permission check status.[" + status.name() + "]");
            }
        }
    }
}
