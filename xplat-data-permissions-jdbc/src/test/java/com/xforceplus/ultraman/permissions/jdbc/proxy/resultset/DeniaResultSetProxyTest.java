package com.xforceplus.ultraman.permissions.jdbc.proxy.resultset;

import com.xforceplus.ultraman.permissions.jdbc.define.ResultSetInvalidValues;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * DeniaResultSetProxy Tester.
 *
 * @author <Authors name>
 * @version 1.0 11/29/2019
 * @since <pre>Nov 29, 2019</pre>
 */
public class DeniaResultSetProxyTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testProxy() throws Exception {
        List<Pack> caseData = buildCase();

        caseData.stream().forEach(p -> {
            DeniaResultSetProxy proxy = new DeniaResultSetProxy(p.target);
            Object value;
            for (int i = 0; i < p.methods.size(); i++) {
                try {
                    value = proxy.invoke(null, p.methods.get(i), p.argsList.get(i));
                } catch (Throwable t) {
                    throw new RuntimeException(t.getMessage(), t);
                }

                Assert.assertEquals(p.expectedValues.get(i), value);
            }
        });
    }

    private class Pack {
        private ResultSet target;
        private List<Method> methods;
        private List<Object[]> argsList;
        private List<Object> expectedValues;

        public Pack(ResultSet target, List<Method> methods, List<Object[]> argsList,
                    List<Object> expectedValues) {
            this.target = target;
            this.methods = methods;
            this.argsList = argsList;
            this.expectedValues = expectedValues;
        }
    }

    private List<Pack> buildCase() throws Exception {
        List<Pack> packs = new ArrayList();
        ResultSet resultSet = buildResultSet();
        packs.add(
            new Pack(
                resultSet,
                Arrays.asList(
                    ResultSet.class.getMethod("next", new Class[0]),
                    ResultSet.class.getMethod("getMetaData", new Class[0]),
                    ResultSet.class.getMethod("getString", Integer.TYPE),
                    ResultSet.class.getMethod("getInt", int.class)
                ),
                Arrays.asList(
                    new Object[0],
                    new Object[0],
                    new Object[] {1},
                    new Object[] {2}
                ),
                Arrays.asList(
                    false,
                    resultSet.getMetaData(),
                    ResultSetInvalidValues.STRING,
                    ResultSetInvalidValues.INT
                )
            )
        );

        return packs;
    }

    private ResultSet buildResultSet() throws Exception {
        ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
        when(resultSetMetaData.getColumnCount()).thenReturn(2);
        when(resultSetMetaData.getColumnName(1)).thenReturn("c1");
        when(resultSetMetaData.getColumnName(2)).thenReturn("c2");

        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);

        when(resultSet.getString(1)).thenReturn("c1.value");
        when(resultSet.getInt(2)).thenReturn(2);

        return resultSet;

    }

} 
