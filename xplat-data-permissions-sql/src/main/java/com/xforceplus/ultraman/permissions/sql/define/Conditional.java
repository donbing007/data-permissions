package com.xforceplus.ultraman.permissions.sql.define;

/**
 * 条件连接符号.
 *
 * @author dongbin
 * @version 0.1 2019/10/25 16:19
 * @since 1.8
 */
public enum Conditional {

    AND("AND"),
    OR("OR");

    private String symbol;

    private Conditional(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Conditional getInstance(String symbol) {
        String noSpaceSymbol = symbol.trim();
        for (Conditional operator : Conditional.values()) {
            if (operator.getSymbol().equals(noSpaceSymbol)) {
                return operator;
            }
        }

        return null;
    }
}
