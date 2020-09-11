package com.xforceplus.ultraman.permissions.jdbc;


import com.xforceplus.ultraman.permissions.jdbc.authorization.AuthorizationSearcher;
import com.xforceplus.ultraman.permissions.jdbc.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.jdbc.parser.VariableParserManager;
import com.xforceplus.ultraman.permissions.jdbc.proxy.PreparedStatementProxy;
import com.xforceplus.ultraman.permissions.jdbc.proxy.StatementProxy;
import com.xforceplus.ultraman.permissions.jdbc.utils.ProxyFactory;
import com.xforceplus.ultraman.permissions.sql.hint.Hint;
import com.xforceplus.ultraman.permissions.sql.hint.parser.HintParser;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 权限连接.
 *
 * @author dongbin
 * @version 0.1 2019/11/15 15:35
 * @since 1.8
 */
public class PermissionsConnectionWrapper implements Connection {

    private RuleCheckServiceClient client;
    private AuthorizationSearcher authorizationSearcher;
    private Connection original;
    private HintParser hintParser;
    private VariableParserManager variableParserManager;

    public PermissionsConnectionWrapper(
            RuleCheckServiceClient client, AuthorizationSearcher authorizationSearcher, Connection original,
            HintParser hintParser) {
        this.client = client;
        this.authorizationSearcher = authorizationSearcher;
        this.original = original;
        this.hintParser = hintParser;
    }

    public PermissionsConnectionWrapper(
            RuleCheckServiceClient client, AuthorizationSearcher authorizationSearcher, Connection original,
            HintParser hintParser, VariableParserManager manager) {
        this.client = client;
        this.authorizationSearcher = authorizationSearcher;
        this.original = original;
        this.hintParser = hintParser;
        this.variableParserManager = manager;
    }

    @Override
    public Statement createStatement() throws SQLException {
        Statement source = original.createStatement();

        return (Statement) ProxyFactory.createInterfacetProxyFromObject(
                source, new StatementProxy(client, authorizationSearcher.search(), source, hintParser));
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return (PreparedStatement) ProxyFactory.createInterfactProxy(PreparedStatement.class,
                new PreparedStatementProxy(
                        client,
                        authorizationSearcher.search(),
                        innerSql -> original.prepareStatement(innerSql),
                        hintParser,
                        sql, variableParserManager
                )
        );
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return original.prepareCall(sql);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        Statement source = original.createStatement(resultSetType, resultSetConcurrency);

        return (Statement) ProxyFactory.createInterfacetProxyFromObject(
                source, new StatementProxy(client, authorizationSearcher.search(), source, hintParser));
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return (PreparedStatement) ProxyFactory.createInterfactProxy(PreparedStatement.class,
                new PreparedStatementProxy(
                        client,
                        authorizationSearcher.search(),
                        innerSql -> original.prepareStatement(innerSql, resultSetType, resultSetConcurrency),
                        hintParser,
                        sql, variableParserManager
                )
        );
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return original.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        Statement source = original.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);

        return (Statement) ProxyFactory.createInterfacetProxyFromObject(
                source, new StatementProxy(client, authorizationSearcher.search(), source, hintParser));
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return (PreparedStatement) ProxyFactory.createInterfactProxy(PreparedStatement.class,
                new PreparedStatementProxy(
                        client,
                        authorizationSearcher.search(),
                        innerSql -> original.prepareStatement(innerSql, resultSetType, resultSetConcurrency, resultSetHoldability),
                        hintParser,
                        sql, variableParserManager
                )
        );
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return original.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return (PreparedStatement) ProxyFactory.createInterfactProxy(PreparedStatement.class,
                new PreparedStatementProxy(
                        client,
                        authorizationSearcher.search(),
                        innerSql -> original.prepareStatement(innerSql, autoGeneratedKeys),
                        hintParser,
                        sql, variableParserManager
                )
        );
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return (PreparedStatement) ProxyFactory.createInterfactProxy(PreparedStatement.class,
                new PreparedStatementProxy(
                        client,
                        authorizationSearcher.search(),
                        innerSql -> original.prepareStatement(innerSql, columnIndexes),
                        hintParser,
                        sql, variableParserManager
                )
        );
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return (PreparedStatement) ProxyFactory.createInterfactProxy(PreparedStatement.class,
                new PreparedStatementProxy(
                        client,
                        authorizationSearcher.search(),
                        innerSql -> original.prepareStatement(innerSql, columnNames),
                        hintParser,
                        sql, variableParserManager
                )
        );
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return original.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        original.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return original.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        original.commit();
    }

    @Override
    public void rollback() throws SQLException {
        original.rollback();
    }

    @Override
    public void close() throws SQLException {
        original.close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return original.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return original.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        original.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return original.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        original.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return original.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        original.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return original.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return original.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        original.clearWarnings();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return original.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        original.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        original.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return original.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return original.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return original.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        original.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        original.releaseSavepoint(savepoint);
    }

    @Override
    public Clob createClob() throws SQLException {
        return original.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return original.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return original.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return original.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return original.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        original.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        original.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return original.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return original.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return original.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return original.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        original.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return original.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        original.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        original.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return original.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return original.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return original.isWrapperFor(iface);
    }
}
