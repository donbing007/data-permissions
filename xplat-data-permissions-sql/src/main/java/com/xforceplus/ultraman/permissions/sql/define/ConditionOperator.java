package com.xforceplus.ultraman.permissions.sql.define;

/**
 * @author dongbin
 * @version 0.1 2019/10/25 16:53
 * @since 1.8
 */
public enum ConditionOperator {
    BETWEEN("BETWEEN"),
    IN("IN"),
    NOT_IN("NOT IN"),
    LIKE("LIKE"),
    NOT_LIKE("NOT LIKE"),
    EQUALS("="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    GREATER_THAN_EQUALS(">="),
    MINOR_THAN("<"),
    MINOR_THAN_EQUALS("<="),
    IS_NOT("IS NOT"),
    IS("IS");

    private String symbol;

    private ConditionOperator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static ConditionOperator getInstance(String symbol) {
        String noSpaceSymbol = symbol.trim();
        for (ConditionOperator operator : ConditionOperator.values()) {
            if (operator.getSymbol().equals(noSpaceSymbol)) {
                return operator;
            }
        }

        return null;
    }
}
