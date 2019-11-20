package com.xforceplus.ultraman.permissions.starter.jdbc.proxy;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.result.CheckStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.starter.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.starter.jdbc.proxy.resultset.DenialResultSet;
import com.xforceplus.ultraman.permissions.starter.jdbc.proxy.resultset.PassResultSetProxy;
import com.xforceplus.ultraman.permissions.starter.utils.MethodHelper;
import com.xforceplus.ultraman.permissions.starter.utils.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 处理 PrepareStatemtn 预处理词句.
 *
 * @version 0.1 2019/11/17 23:11
 * @auth dongbin
 * @since 1.8
 */
public class PreparedStatementProxy extends AbstractStatementProxy implements InvocationHandler {

    private static final Class[] EXPECTED_PARAMETERS_TYPE = new Class[0];
    private String sql;
    private PreparedStatementMaker maker;
    private CheckResult checkResult;
    private PreparedStatement sourcePreparedStatement;
    private boolean refuse;

    public PreparedStatementProxy(
        RuleCheckServiceClient client, Authorization authorization, PreparedStatementMaker maker, String sql)
        throws SQLException {

        super(client, authorization);
        this.sql = sql;
        this.maker = maker;

        check();
    }

    public boolean isRefuse() {
        return refuse;
    }

    public CheckResult getCheckResult() {
        return checkResult;
    }

    private void check() throws SQLException {
        checkResult = getClient().check(sql, getAuthorization());
        CheckStatus status = CheckStatus.getInstance(checkResult.getCode());
        switch (status) {
            case PASS: {
                sourcePreparedStatement = maker.make(sql);
                break;
            }
            case UPDATE: {
                if (checkResult.getNewSql() == null) {
                    throw new IllegalStateException("The status is updated, but no replacement SQL statement was found!");
                }
                sourcePreparedStatement = maker.make(checkResult.getNewSql());
                break;
            }
            case DENIAL: {
                refuse = true;
                break;
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


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (MethodHelper.isTarget(method, "executeQuery", EXPECTED_PARAMETERS_TYPE, ResultSet.class)
            || MethodHelper.isTarget(method, "executeUpdate", EXPECTED_PARAMETERS_TYPE, Integer.TYPE)) {

            if (refuse) {

                if (method.getReturnType().equals(Integer.TYPE)) {
                    return 0;
                } else {
                    return DenialResultSet.getInstance();
                }
            } else {

                Object value = method.invoke(sourcePreparedStatement, args);
                if (method.getReturnType().equals(ResultSet.class)) {
                    return ProxyFactory.createInterfaceProxy(value,
                        new PassResultSetProxy(checkResult.getBackList(), (ResultSet) value));
                } else {

                    return value;
                }

            }
        } else {

            return method.invoke(sourcePreparedStatement, args);
        }
    }

    /**
     * PreparedStatement的构造器,由创建者传入.
     */
    public interface PreparedStatementMaker {

        /**
         * 构造一个新的PreparedStatement实例.
         *
         * @param sql 目标 sql.
         * @return 实例.
         * @throw SQLException 构造失败.
         */
        PreparedStatement make(String sql) throws SQLException;
    }

}
