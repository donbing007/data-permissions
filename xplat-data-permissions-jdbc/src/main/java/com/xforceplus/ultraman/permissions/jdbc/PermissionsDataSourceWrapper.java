package com.xforceplus.ultraman.permissions.jdbc;


import com.xforceplus.ultraman.permissions.jdbc.authorization.AuthorizationSearcher;
import com.xforceplus.ultraman.permissions.jdbc.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.sql.hint.parser.HintParser;
import com.xforceplus.ultraman.permissions.sql.hint.parser.SQL92HintParser;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 数据源包装.
 *
 * @author dongbin
 * @version 0.1 2019/11/15 15:33
 * @since 1.8
 */
public class PermissionsDataSourceWrapper implements DataSource {

    private RuleCheckServiceClient client;
    private AuthorizationSearcher authorizationSearcher;
    private DataSource original;
    private HintParser hintParser;

    public PermissionsDataSourceWrapper(RuleCheckServiceClient client, AuthorizationSearcher authorizationSearcher, DataSource original) {
        this.client = client;
        this.authorizationSearcher = authorizationSearcher;
        this.original = original;
        this.hintParser = new SQL92HintParser();
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = original.getConnection();
        return new PermissionsConnectionWrapper(client, authorizationSearcher, conn, hintParser);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Connection conn = original.getConnection(username, password);
        return new PermissionsConnectionWrapper(client, authorizationSearcher, conn, hintParser);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return original.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return original.isWrapperFor(iface);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return original.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        original.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        original.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return original.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return original.getParentLogger();
    }

    public DataSource getOriginal() {
        return original;
    }
}
