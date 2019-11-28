package com.xforceplus.ultraman.permissions.jdbc.utils;


import com.xforceplus.ultraman.permissions.jdbc.define.ResultSetInvalidValues;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;

/**
 * 转换帮助类.
 * @version 0.1 2019/11/17 21:39
 * @auth dongbin
 * @since 1.8
 */
public class ConvertingHelper {

    /**
     * 转换实际类型的默认无权限值.
     * @param clazz 目标类型.
     * @return 无权限值.
     */
    public static Object convertInvalidValue(Class clazz) {
        if (String.class.equals(clazz)) {
            return ResultSetInvalidValues.STRING;
        } else if (Boolean.TYPE.equals(clazz)) {
            return ResultSetInvalidValues.BOOLEAN;
        } else if (Byte.TYPE.equals(clazz)) {
            return ResultSetInvalidValues.BYTE;
        } else if (byte[].class.equals(clazz)) {
            return ResultSetInvalidValues.BYTES;
        } else if (Short.TYPE.equals(clazz)) {
            return ResultSetInvalidValues.SHORT;
        } else if (Integer.TYPE.equals(clazz)) {
            return ResultSetInvalidValues.INT;
        } else if (Long.TYPE.equals(clazz)) {
            return ResultSetInvalidValues.LONG;
        } else if (Float.TYPE.equals(clazz)) {
            return ResultSetInvalidValues.FLOAT;
        } else if (Double.TYPE.equals(clazz)) {
            return ResultSetInvalidValues.DOUBLE;
        } else if (BigDecimal.class.equals(clazz)) {
            return ResultSetInvalidValues.BIG_DECIMAL;
        } else if (Date.class.equals(clazz)) {
            return ResultSetInvalidValues.DATE;
        } else if (Time.class.equals(clazz)) {
            return ResultSetInvalidValues.TIME;
        } else if (Timestamp.class.equals(clazz)) {
            return ResultSetInvalidValues.TIMESTAMP;
        } else if (InputStream.class.equals(clazz)) {
            return ResultSetInvalidValues.STREAM;
        } else if (Reader.class.equals(clazz)) {
            return ResultSetInvalidValues.READER;
        } else if (Blob.class.equals(clazz)) {
            return ResultSetInvalidValues.BLOB;
        } else if (Clob.class.equals(clazz)) {
            return ResultSetInvalidValues.CLOB;
        } else if (Ref.class.equals(clazz)) {
            return ResultSetInvalidValues.REF;
        } else {
            return null;
        }
    }
}
