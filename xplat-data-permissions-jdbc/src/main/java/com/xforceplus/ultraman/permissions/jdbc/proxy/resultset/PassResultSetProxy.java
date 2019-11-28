package com.xforceplus.ultraman.permissions.jdbc.proxy.resultset;


import com.xforceplus.ultraman.permissions.jdbc.utils.ConvertingHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * @version 0.1 2019/11/17 22:21
 * @auth dongbin
 * @since 1.8
 */
public class PassResultSetProxy implements InvocationHandler {


    private List<String> blackList;
    private ResultSet target;

    public PassResultSetProxy(List<String> blackList, ResultSet target) {
        this.blackList = blackList;
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isGetMethod(method)) {
            return doGetMethod(method, args);
        }

        if (isUpdate(method)) {
            return doUpdateMethod(method, args);
        }

        return method.invoke(target, args);
    }

    // 如果在黑名单,那么将无法更新.
    private Object doUpdateMethod(Method method, Object[] args) throws Throwable{

        String columnName = findColumnName(method, args);

        if (!isInBlackList(columnName)) {
            return method.invoke(target, args);
        }

        return null;
    }

    // 如果在黑名单,那么将得到无效数据.
    private Object doGetMethod(Method method, Object[] args) throws Throwable{

        String columnName = findColumnName(method, args);

        if (isInBlackList(columnName)) {

            return ConvertingHelper.convertInvalidValue(method.getReturnType());

        } else {

            return method.invoke(target, args);
        }
    }

    private String findColumnName(Method method, Object[] args) throws SQLException {
        if (Integer.TYPE.equals(method.getParameterTypes()[0])) {
            ResultSetMetaData metaData = target.getMetaData();
            return metaData.getColumnName((int) args[0]);
        } else {
            return (String) args[0];
        }

    }

    private boolean isInBlackList(String columnName) {
        return blackList.contains(columnName);
    }

    private boolean isGetMethod(Method method) {
        return method.getName().startsWith("get");
    }

    private boolean isUpdate(Method method) {
        return method.getName().startsWith("update");
    }

}
