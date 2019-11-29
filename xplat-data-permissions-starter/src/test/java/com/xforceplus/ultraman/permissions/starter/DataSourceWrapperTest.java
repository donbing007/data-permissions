package com.xforceplus.ultraman.permissions.starter;

import com.xforceplus.ultraman.permissions.jdbc.authorization.AuthorizationSearcher;
import com.xforceplus.ultraman.permissions.jdbc.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.starter.define.BeanNameDefine;
import com.xforceplus.ultraman.permissions.starter.jdbc.PermissionsDataSourceWrapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * DataSourceWrapper Tester.
 *
 * @author <Authors name>
 * @version 1.0 11/29/2019
 * @since <pre>Nov 29, 2019</pre>
 */
public class DataSourceWrapperTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: wrap(DataSource dataSource)
     */
    @Test
    public void testWrap() throws Exception {

        DataSourceWrapper wrapper = new DataSourceWrapper();

        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBean(BeanNameDefine.RULE_CHECK_CLIENT)).thenReturn(new RuleCheckServiceClient() {
            @Override
            public CheckResult check(String sql, Authorizations authorizations) {
                return null;
            }
        });
        when(context.getBean(BeanNameDefine.AUTHORIZATION_SEARCHER)).thenReturn(new AuthorizationSearcher() {
            @Override
            public Authorizations search() {
                return null;
            }
        });

        wrapper.setApplicationContext(context);
        DataSource dataSource  = wrapper.wrap(new TestDataSource("d1"));
        Assert.assertEquals(PermissionsDataSourceWrapper.class, dataSource.getClass());

        dataSource  = wrapper.wrap(
            new PermissionsDataSourceWrapper(null, null, new TestDataSource("d2")));
        Assert.assertEquals(PermissionsDataSourceWrapper.class, dataSource.getClass());
        Assert.assertEquals("d2",
            ((TestDataSource)((PermissionsDataSourceWrapper) dataSource).getOriginal()).getName());

    }

    static class TestDataSource implements DataSource {

        private String name;

        public TestDataSource(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DataSourceInterceptorTest.TestDataSource)) return false;
            DataSourceInterceptorTest.TestDataSource that = (DataSourceInterceptorTest.TestDataSource) o;
            return Objects.equals(getName(), that.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName());
        }

        @Override
        public Connection getConnection() throws SQLException {
            return null;
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return null;
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {

        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {

        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }
    }


} 
