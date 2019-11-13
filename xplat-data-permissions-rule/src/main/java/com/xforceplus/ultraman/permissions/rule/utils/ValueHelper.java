package com.xforceplus.ultraman.permissions.rule.utils;

/**
 * @version 0.1 2019/11/11 18:25
 * @auth dongbin
 * @since 1.8
 */
public class ValueHelper {

    private static final String FLAG = ",";

    public static String[] parserMultipleValue(String value) {
        return value.split(FLAG);
    }

}
