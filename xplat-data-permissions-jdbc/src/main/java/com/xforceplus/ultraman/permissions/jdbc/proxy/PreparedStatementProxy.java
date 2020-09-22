package com.xforceplus.ultraman.permissions.jdbc.proxy;

import com.xforceplus.ultraman.permissions.jdbc.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.jdbc.define.ResultSetInvalidValues;
import com.xforceplus.ultraman.permissions.jdbc.parser.Variable;
import com.xforceplus.ultraman.permissions.jdbc.parser.VariableParserManager;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 处理 PrepareStatemtn 预处理词句.
 *
 * @author dongbin
 * @version 0.1 2019/11/17 23:11
 * @since 1.8
 */
public class PreparedStatementProxy extends AbstractStatementProxy implements InvocationHandler {

    private static final String[] FORCE_METHODS = new String[]{
            "executeQuery",
            "executeUpdate",
            "execute",
            "executeLargeUpdate",
            "getResultSet",
            "getUpdateCount",
            "getMoreResults"
    };

    final Logger logger = LoggerFactory.getLogger(StatementProxy.class);
    private String sql;
    private PreparedStatementMaker maker;
    private CheckResult checkResult;
    private PreparedStatement sourcePreparedStatement;
    private boolean refuse;

    public PreparedStatementProxy(
            RuleCheckServiceClient client,
            Authorizations authorizations,
            PreparedStatementMaker maker,
            HintParser hintParser,
            String sql,
            VariableParserManager manager)
            throws SQLException {

        super(client, authorizations, hintParser, manager);
        this.sql = sql;
        this.maker = maker;

        check();

        if (DebugStatus.isDebug()) {
            logger.debug("Expected: {}", sql);
        }
    }

    public PreparedStatementProxy(
            RuleCheckServiceClient client,
            Authorizations authorizations,
            PreparedStatementMaker maker,
            HintParser hintParser,
            String sql)
            throws SQLException {

        super(client, authorizations, hintParser);
        this.sql = sql;
        this.maker = maker;

        check();

        if (DebugStatus.isDebug()) {
            logger.debug("Expected: {}", sql);
        }
    }

    public boolean isRefuse() {
        return refuse;
    }

    public CheckResult getCheckResult() {
        return checkResult;
    }

    private void check() throws SQLException {
        Hint hint = getHintParser().parse(sql);
        if (hint.isIgnore()) {
            sourcePreparedStatement = maker.make(sql);
            refuse = false;
            checkResult = new CheckResult(CheckStatus.PASS);

            if (DebugStatus.isDebug()) {
                logger.debug("Ignore: {}", sql);
            }

        } else {

            checkResult = getClient().check(sql, getAuthorization());
            CheckStatus status = checkResult.getStatus();
            switch (status) {
                case PASS: {

                    if (DebugStatus.isDebug()) {
                        logger.debug("Actual: {}", sql);
                    }

                    sourcePreparedStatement = maker.make(sql);
                    refuse = false;
                    break;
                }
                case UPDATE: {
                    String newSql = checkResult.findFirst().get().getNewSql();
                    if (newSql == null) {
                        throw new IllegalStateException("The status is updated, but no replacement SQL statement was found!");
                    }
                    String variableSql = manager.parse(newSql);
                    if (DebugStatus.isDebug()) {
                        logger.debug("Actual: {}", newSql);
                        logger.debug("Actual variable sql : {}", variableSql);
                    }
                    sourcePreparedStatement = maker.make(variableSql);
                    refuse = false;

                    break;
                }
                case DENIAL: {

                    if (DebugStatus.isDebug()) {
                        logger.debug("Actual: DENIAL, cause {}.", checkResult.getMessage());
                    }

                    sourcePreparedStatement = maker.make(sql);
                    refuse = true;
                    break;
                }
                case ERROR: {
                    String message = checkResult.getMessage();

                    /**
                     * 此段是为了兼容老的服务端.老服务端不会抛出 NOT_SUPPORT 状态,以 ERROR 方式提供.
                     */
                    if ("Unsupported SQL.".equals(message)) {
                        logger.warn("Unsupported statement.[{}]", sql);
                        refuse = false;
                        break;
                    }

                    refuse = true;

                    throw new SQLException(message != null ? message : "");
                }
                case NOT_SUPPORT: {
                    logger.warn("Unsupported statement.[{}]", sql);
                    refuse = false;
                    break;
                }
                default: {
                    refuse = true;
                    throw new SQLException("Unknown permission check status.[" + status.name() + "]");
                }
            }
        }
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /**
         * 不管拒绝与否都会执行底层的.
         */
        Object executeResult = method.invoke(sourcePreparedStatement, args);

        if (isForceMethod(method, FORCE_METHODS)) {

            if (refuse) {

                if (method.getReturnType().equals(Integer.TYPE)) {
                    return ResultSetInvalidValues.INT;
                } else if (method.getReturnType().equals(Boolean.TYPE)) {
                    return ResultSetInvalidValues.BOOLEAN;
                } else {
                    ResultSet resultSet = (ResultSet) method.invoke(sourcePreparedStatement, args);
                    return ProxyFactory.createInterfactProxy(
                            ResultSet.class,
                            new DeniaResultSetProxy(resultSet)
                    );
                }
            } else {

                if (method.getReturnType().equals(ResultSet.class)) {
                    Optional<SqlChange> sqlChangeOptional = checkResult.findFirst();
                    List<String> blackList = Collections.emptyList();
                    if (sqlChangeOptional.isPresent()) {
                        blackList = sqlChangeOptional.get().getBlackList();
                    }
                    return ProxyFactory.createInterfactProxy(ResultSet.class,
                            new PassResultSetProxy(blackList, (ResultSet) executeResult));

                } else {

                    return executeResult;
                }

            }

        } else {

            return executeResult;
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
