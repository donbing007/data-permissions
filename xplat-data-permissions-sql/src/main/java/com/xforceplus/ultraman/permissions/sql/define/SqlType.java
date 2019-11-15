package com.xforceplus.ultraman.permissions.sql.define;

/**
 * SQL 语句类型.
 *
 * @author dongbin
 * @version 0.1 2019/10/25 16:21
 * @since 1.8
 */
public enum SqlType {

    UNKNOWN("UNKNOWN"),
    SELECT("SELECT"),
    DELETE("DELETE"),
    UPDATE("UPDATE"),
    INSERT("INSERT");

    private String symbol;

    private SqlType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static SqlType getInstance(String symbol) {
        String noSpaceSymbol = symbol.trim();
        for (SqlType operator : SqlType.values()) {
            if (operator.getSymbol().equals(noSpaceSymbol)) {
                return operator;
            }
        }

        return UNKNOWN;
    }
}
