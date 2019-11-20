package com.xforceplus.ultraman.permissions.pojo.rule;

/**
 * 条件之间的联系.
 * @version 0.1 2019/11/6 16:13
 * @author dongbin
 * @since 1.8
 */
public enum RuleConditionRelationship {

    AND(0),
    OR(1);

    private int symbol;

    private RuleConditionRelationship(int symbol) {
        this.symbol = symbol;
    }

    public int getSymbol() {
        return symbol;
    }

    public static RuleConditionRelationship getInstance(int symbol) {
        for (RuleConditionRelationship operator : RuleConditionRelationship.values()) {
            if (operator.getSymbol() == symbol) {
                return operator;
            }
        }

        return null;
    }
}
