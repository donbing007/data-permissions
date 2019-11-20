package com.xforceplus.ultraman.permissions.pojo.rule;

/**
 * 规则中可以用的条件.
 * @version 0.1 2019/11/6 16:03
 * @author dongbin
 * @since 1.8
 */
public enum RuleConditionOperation {
    GREATER(">"),
    LESS("<"),
    EQUAL("="),
    NOT_EQUAL("!="),
    GREATER_EQUAL(">="),
    LESS_EQUAL("<="),
    LIST("[]"),
    BEFORE("^"),
    AFTER("$"),
    NOT_BEFORE("!^"),
    NOT_AFTER("!$"),
    CONTAINS("()");

    private String symbol;

    private RuleConditionOperation(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static RuleConditionOperation getInstance(String symbol) {
        String noSpaceSymbol = symbol.trim();
        for (RuleConditionOperation operator : RuleConditionOperation.values()) {
            if (operator.getSymbol().equals(noSpaceSymbol)) {
                return operator;
            }
        }

        return null;
    }
}
