package com.xforceplus.ultraman.permissions.starter.jdbc.proxy;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.result.CheckStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.starter.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.starter.jdbc.proxy.resultset.DenialResultSet;
import com.xforceplus.ultraman.permissions.starter.utils.ProxyFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * PreparedStatementProxy Tester.
 *
 * @author <Authors name>
 * @version 1.0 11/19/2019
 * @since <pre>Nov 19, 2019</pre>
 */
public class PreparedStatementProxyTest {

    private Authorization auth = new Authorization("r1", "t1");

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * 测试无权限.
     */
    @Test
    public void testRefuse() throws Exception {

        Map<String, Pack> caseData = buildCase();
        caseData.keySet().stream().forEach(sql -> {


            Pack pack = caseData.get(sql);
            RuleCheckServiceClient client = mock(RuleCheckServiceClient.class);
            when(client.check(sql, auth)).thenReturn(pack.checkReuslt);

            PreparedStatementProxy proxy = null;
            try {
                proxy = new PreparedStatementProxy(client, auth, pack.maker, sql);
                if (pack.expectedCheckException != null) {
                    Assert.fail("An exception should be thrown, but it is not.");
                }
            } catch (SQLException ex) {
                if (pack.expectedCheckException == null) {
                    Assert.fail("An exception should not be thrown, but it was.");
                }
            }

            if (proxy != null) {
                Object value = null;
                try {
                    value = proxy.invoke(null, pack.method, pack.args);


                } catch (Throwable ex) {
                    throw new RuntimeException(ex.getMessage(), ex);
                }

                if (value == null) {
                    Assert.assertNull(pack.expectedValueClass);
                } else {
                    Assert.assertTrue(pack.expectedValueClass.isInstance(value));
                }
            }
        });

    }

    private Map<String, Pack> buildCase() throws Exception {
        Map<String, Pack> data = new LinkedHashMap<>();

        data.put("select * from t1 where c1 = ?",
            new Pack(
                new CheckResult(CheckStatus.DENIAL.getValue()),
                sql -> null,
                PreparedStatement.class.getMethod("executeQuery", new Class[0]),
                new Object[0],
                DenialResultSet.class,
                null
            )
        );

        data.put("select * from t1 where c1=?",
            new Pack(
                new CheckResult(CheckStatus.PASS.getValue()),
                sql -> {
                    return (PreparedStatement) ProxyFactory.createInterfactProxy(PreparedStatement.class, (proxy, method, args) -> {
                        if (method.getReturnType().equals(ResultSet.class)) {
                            return ProxyFactory.createInterfactProxy(ResultSet.class,new InvocationHandler() {

                                @Override
                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                    // do nothing
                                    return null;
                                }
                            });
                        } else {

                            return null;
                        }
                    });
                },
                PreparedStatement.class.getMethod("executeQuery", new Class[0]),
                new Object[0],
                ResultSet.class,
                null
            )
        );


        return data;
    }

    private static class Pack {
        private CheckResult checkReuslt;
        private PreparedStatementProxy.PreparedStatementMaker maker;
        private Method method;
        private Object[] args;
        private Class expectedValueClass;
        private Throwable expectedCheckException;

        public Pack(CheckResult checkReuslt,
                    PreparedStatementProxy.PreparedStatementMaker maker,
                    Method method,
                    Object[] args,
                    Class expectedValueClass,
                    Throwable expectedCheckException) {
            this.checkReuslt = checkReuslt;
            this.maker = maker;
            this.method = method;
            this.args = args;
            this.expectedValueClass = expectedValueClass;
            this.expectedCheckException = expectedCheckException;
        }
    }

} 
