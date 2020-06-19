package com.xforceplus.ultraman.permissions.jdbc.proxy;

import com.xforceplus.ultraman.permissions.jdbc.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.jdbc.define.ResultSetInvalidValues;
import com.xforceplus.ultraman.permissions.jdbc.proxy.resultset.DeniaResultSetProxy;
import com.xforceplus.ultraman.permissions.jdbc.proxy.resultset.PassResultSetProxy;
import com.xforceplus.ultraman.permissions.jdbc.utils.DebugStatus;
import com.xforceplus.ultraman.permissions.jdbc.utils.MethodHelper;
import com.xforceplus.ultraman.permissions.jdbc.utils.ProxyFactory;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.pojo.check.SqlChange;
import com.xforceplus.ultraman.permissions.pojo.result.CheckStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.sql.hint.Hint;
import com.xforceplus.ultraman.permissions.sql.hint.parser.HintParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 处理 statement 类型.
 * 会关注以下两个方法.
 * ResultSet executeQuery(String sql) throws SQLException;
 * int executeUpdate(String sql) throws SQLException;
 * 拦截其方法的执行,在执行之前进行权限验证.
 *
 * @author dongbin
 * @version 0.1 2019/11/15 17:11
 * @see Statement
 * @since 1.8
 */
public class StatementProxy extends AbstractStatementProxy implements InvocationHandler {

    private static final String[] FORCE_METHODS = new String[]{
        "executeQuery",
        "executeUpdate",
        "execute"
    };

    final Logger logger = LoggerFactory.getLogger(StatementProxy.class);
    private Statement statement;

    public StatementProxy(RuleCheckServiceClient client, Authorizations authorizations, Statement statement, HintParser hintParser) {
        super(client, authorizations, hintParser);
        this.statement = statement;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (isForceMethod(method, FORCE_METHODS)) {
            return doExecute(method, args);
        } else {
            return method.invoke(statement, args);
        }
    }

    private Object doExecute(Method method, Object[] args) throws Throwable {
        String sql = (String) args[0];

        return doCheck(sql, method, args);

    }

    protected Object doCheck(String sql, Method method, Object[] args) throws Throwable {
        Hint hint = getHintParser().parse(sql);
        CheckStatus status;
        CheckResult checkResult;
        if (hint.isIgnore()) {

            checkResult = new CheckResult(CheckStatus.PASS);

            if (DebugStatus.isDebug()) {
                logger.debug("Ignore: {}", sql);
            }

        } else {

            checkResult = getClient().check(sql, getAuthorization());
        }

        status = checkResult.getStatus();

        if (DebugStatus.isDebug()) {
            logger.debug("Expected: {}", sql);
        }

        switch (status) {
            case PASS: {

                if (DebugStatus.isDebug()) {
                    logger.debug("Actual: {}", sql);
                }

                if (!ResultSet.class.equals(method.getReturnType())) {

                    return method.invoke(statement, args);

                } else {

                    ResultSet target = (ResultSet) method.invoke(statement, args);

                    Optional<SqlChange> firstSqlChange = checkResult.findFirst();
                    List<String> blackList = Collections.emptyList();
                    if (firstSqlChange.isPresent()) {
                        blackList = firstSqlChange.get().getBlackList();
                    }

                    return ProxyFactory.createInterfacetProxyFromObject(
                        target,
                        new PassResultSetProxy(blackList, target));
                }
            }
            case UPDATE: {

                String newSql = checkResult.findFirst().get().getNewSql();
                if (newSql == null) {
                    throw new IllegalStateException("The status is updated, but no replacement SQL statement was found!");
                }

                if (DebugStatus.isDebug()) {
                    logger.debug("Actual: {}", newSql);
                }

                if (!ResultSet.class.equals(method.getReturnType())) {

                    return method.invoke(statement, args);

                } else {
                    Optional<SqlChange> firstSqlChange = checkResult.findFirst();
                    ResultSet target = (ResultSet) method.invoke(statement, new Object[]{firstSqlChange.get().getNewSql()});
                    return ProxyFactory.createInterfactProxy(
                        ResultSet.class,
                        new PassResultSetProxy(firstSqlChange.get().getBlackList(), target));
                }
            }
            case DENIAL: {

                if (DebugStatus.isDebug()) {
                    logger.debug("Actual: DENIAL, cause {}", checkResult.getMessage());
                }

                Object result = method.invoke(statement, new Object[]{sql});

                if (Integer.TYPE.equals(method.getReturnType())) {

                    return ResultSetInvalidValues.INT;

                } else if (Boolean.TYPE.equals(method.getReturnType())) {

                    return ResultSetInvalidValues.BOOLEAN;

                } else {

                    ResultSet target = (ResultSet) result;
                    return ProxyFactory.createInterfactProxy(ResultSet.class, new DeniaResultSetProxy(target));

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
