package com.xforceplus.ultraman.permissions.jdbc.proxy.resultset;


import com.xforceplus.ultraman.permissions.jdbc.utils.ConvertingHelper;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * @author dongbin
 * @version 0.1 2019/11/17 22:21
 * @since 1.8
 */
public class PassResultSetProxy extends AbstractResultSetProxy {

    private List<String> blackList;

    public PassResultSetProxy(List<String> blackList, ResultSet target) {
        super(target);
        this.blackList = blackList;
    }

    // 如果在黑名单,那么将无法更新.
    @Override
    protected Object doUpdateMethod(Method method, Object[] args) throws Exception {
        String columnName = findColumnName(method, args);

        if (!isInBlackList(columnName)) {
            return method.invoke(getTarget(), args);
        }

        return null;
    }

    // 如果在黑名单,那么将得到无效数据.
    @Override
    protected Object doGetMethod(Method method, Object[] args) throws Exception {
        if (method.getName().equals("getStatement")) {
            return null;
        }

        String columnName = findColumnName(method, args);

        if (isInBlackList(columnName)) {

            return ConvertingHelper.convertInvalidValue(method.getReturnType());

        } else {

            return method.invoke(getTarget(), args);
        }
    }

    private String findColumnName(Method method, Object[] args) throws SQLException {
        if (Integer.TYPE.equals(method.getParameterTypes()[0])) {
            ResultSetMetaData metaData = getTarget().getMetaData();
            return metaData.getColumnName((int) args[0]);
        } else {
            return (String) args[0];
        }

    }

    private boolean isInBlackList(String columnName) {
        return blackList.contains(columnName);
    }

}
