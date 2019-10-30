package com.xforceplus.ultraman.permissions.sql.jsqlparser.utils;

import com.xforceplus.ultraman.permissions.sql.define.values.ArithmeticValue;
import com.xforceplus.ultraman.permissions.sql.define.values.UnknownValue;
import com.xforceplus.ultraman.permissions.sql.define.values.Value;
import net.sf.jsqlparser.expression.*;

/**
 * JSqlParser 值类型的转换工厂.
 * @version 0.1 2019/10/29 15:42
 * @auth dongbin
 * @since 1.8
 */
public final class ValueHelper {

    /**
     * value 类型 expr 的实例包名.
     */
    private static final String VALUE_PACKGE = "net.sf.jsqlparser.expression";

    /**
     * 运算类型 expr 实例包名.
     */
    private static final String ARITHMETIC_PACKAGE = "net.sf.jsqlparser.expression.operators.arithmetic";



    /**
     * 判断是否为一个值类型表达式.
     * @param expr 表达式.
     * @return true 是值表示达式,false 不是.
     */
    public static final boolean isValueExpr(Expression expr) {
        Class clazz = expr.getClass();
        return isPointPackage(clazz, VALUE_PACKGE) && clazz.getSimpleName().endsWith("Value");
    }

    /**
     * 判断是否为一个运算表达式.
     * @param expr 表达式.
     * @return true 是值表达式,false 不是.
     */
    public static final boolean isArithmeticExpr(Expression expr) {
        Class clazz = expr.getClass();
        return isPointPackage(clazz, ARITHMETIC_PACKAGE);
    }

    private static final boolean isPointPackage(Class clazz, String packString) {
        Package pack = clazz.getPackage();
        return pack.getName().equals(packString);
    }
}
