package com.xforceplus.ultraman.permissions.jdbc.utils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @version 0.1 2019/11/18 16:43
 * @author dongbin
 * @since 1.8
 */
public class MethodHelper {

    private MethodHelper() {}

    /**
     * 判断是否目标方法.
     * @param method 方法.
     * @param name 名称.
     * @param expectedParameterTypes 参数.
     * @param retyrnType 返回值类型.
     * @return true 符合条件,false 不符合.
     */
    public static boolean isTarget(Method method, String name, Class[] expectedParameterTypes, Class retyrnType) {
        if (method.getName().equals(name)) {

            Class[] pClazzs = method.getParameterTypes();
            if (pClazzs != null) {

                if (Arrays.equals(expectedParameterTypes, pClazzs)) {
                    return method.getReturnType().equals(retyrnType);
                }

            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * 判断是否一个前辍的方法.
     * @param method 方法.
     * @param prefixName 方法名前辍.
     * @return true 符合条件,false 不符合.
     */
    public static boolean isTargetPrefix(Method method, String prefixName, boolean oneParameter) {
        String name = method.getName();
        if (name.startsWith(prefixName)) {
            if (oneParameter) {
                final int onlyOne = 1;
                return method.getParameterCount() == onlyOne;
            } else {
                return true;
            }
        }

        return false;
    }
}
