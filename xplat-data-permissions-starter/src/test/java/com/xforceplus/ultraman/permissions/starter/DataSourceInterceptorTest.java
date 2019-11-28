package com.xforceplus.ultraman.permissions.starter;

import com.xforceplus.ultraman.permissions.starter.jdbc.PermissionsDataSourceWrapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * DataSourceInterceptor Tester.
 *
 * @author <Authors name>
 * @version 1.0 11/28/2019
 * @since <pre>Nov 28, 2019</pre>
 */
public class DataSourceInterceptorTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: postProcessAfterInitialization(Object bean, String beanName)
     */
    @Test
    public void testPostProcessAfterInitialization() throws Exception {
        Map<TestDataSource, Pack> caseData = buildCase();
        caseData.keySet().stream().forEach(d -> {

            Pack pack = caseData.get(d);
            DataSourceInterceptor interceptor = new DataSourceInterceptor(pack.includeBeanNames);
            Object value = interceptor.postProcessAfterInitialization(d, d.getName());

            Assert.assertEquals(pack.expectedReturnClass, value.getClass());

        });

    }

    static class Pack {
        private Object expectedReturnClass;
        private String[] includeBeanNames;

        public Pack(Class expectedReturnClass, String[] includeBeanNames) {
            this.expectedReturnClass = expectedReturnClass;
            this.includeBeanNames = includeBeanNames;
        }
    }

    private Map<TestDataSource, Pack> buildCase() {
        Map<TestDataSource, Pack> data = new LinkedHashMap<>();

        // 包含所有
        data.put(new TestDataSource("t0"),
            new Pack(
                PermissionsDataSourceWrapper.class,
                new String[] {"*"}
            )
        );

        // 不包含,原样返回.
        data.put(new TestDataSource("t1"),
            new Pack(
                TestDataSource.class,
                new String[] {"t2","t3"}
            )
        );

        // 包含,饱以老拳.
        data.put(new TestDataSource("t2"),
            new Pack(
                PermissionsDataSourceWrapper.class,
                new String[] {"t2","t3"}
            )
        );

        // 包含,饱以老拳.
        data.put(new TestDataSource("t3"),
            new Pack(
                PermissionsDataSourceWrapper.class,
                new String[] {"*","t2","t3"}
            )
        );

        return data;
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
            if (!(o instanceof TestDataSource)) return false;
            TestDataSource that = (TestDataSource) o;
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
