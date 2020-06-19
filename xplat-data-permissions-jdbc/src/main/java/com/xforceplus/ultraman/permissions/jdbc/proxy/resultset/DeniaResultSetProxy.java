package com.xforceplus.ultraman.permissions.jdbc.proxy.resultset;

import com.xforceplus.ultraman.permissions.jdbc.utils.ConvertingHelper;

import java.lang.reflect.Method;
import java.sql.ResultSet;

/**
 * 拒绝执行的代理实现.
 *
 * @author dongbin
 * @version 0.1 2019/11/29 16:54
 * @since 1.8
 */
public class DeniaResultSetProxy extends AbstractResultSetProxy {


    public DeniaResultSetProxy(ResultSet target) {
        super(target);
    }

    @Override
    protected Object doUpdateMethod(Method method, Object[] args) throws Exception {
        // do nothing.
        return null;
    }

    @Override
    protected Object doGetMethod(Method method, Object[] args) throws Exception {

        // return default null value.
        return ConvertingHelper.convertInvalidValue(method.getReturnType());

    }

    @Override
    protected Object doNext(Method method) throws Exception {
        return false;
    }

}
