package com.xforceplus.ultraman.permissions.jdbc.proxy.resultset;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ResultSet 代理抽像.
 *
 * @author dongbin
 * @version 0.1 2019/11/29 17:01
 * @since 1.8
 */
public abstract class AbstractResultSetProxy implements InvocationHandler {

    private ResultSet target;

    public AbstractResultSetProxy(ResultSet target) {
        this.target = target;
    }

    public ResultSet getTarget() {
        return target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (isNext(method)) {
                return doNext(method);
            }
            if (isGetMetaData(method)) {
                return doGetMetaData(method);
            }

            if (isGetStatement(method)) {
                return doGetStatement(method);
            }

            if (isGetMethod(method)) {
                return doGetMethod(method, args);
            }

            if (isUpdate(method)) {
                return doUpdateMethod(method, args);
            }
        } catch (Exception ex) {
            throw new SQLException(ex.getMessage(), ex);
        }

        return method.invoke(target, args);
    }

    private boolean isNext(Method method) {
        return method.getName().equals("next") && method.getParameterCount() == 0;
    }

    protected abstract Object doUpdateMethod(Method method, Object[] args) throws Exception;

    protected abstract Object doGetMethod(Method method, Object[] args) throws Exception;

    protected Object doNext(Method method) throws Exception {
        return target.next();
    }

    protected Object doGetStatement(Method method) throws Exception {
        return null;
    }

    protected Object doGetMetaData(Method method) throws Exception {
        return target.getMetaData();
    }


    private boolean isGetMetaData(Method method) {
        return method.getName().equals("getMetaData") && method.getParameterCount() == 0;
    }

    private boolean isGetStatement(Method method) {
        return method.getName().equals("getStatement") && method.getParameterCount() == 0;
    }

    private boolean isGetMethod(Method method) {
        return method.getName().startsWith("get") && method.getParameterCount() == 1;
    }

    private boolean isUpdate(Method method) {
        return method.getName().startsWith("update");
    }
}
