package com.xforceplus.ultraman.permissions.pojo.rule;

/**
 * 规则条件中的值类型.
 * @version 0.1 2019/11/6 16:07
 * @author dongbin
 * @since 1.8
 */
public enum RuleConditionValueType {
    UNKNOWN(0), // 未知
    INTEGER(1), // 整形
    STRING(2), // 字符串
    FLOAT(3), // 浮点
    DATATIME(4); // 日期/时间


    private int symbol;

    private RuleConditionValueType(int symbol) {
        this.symbol = symbol;
    }

    public int getSymbol() {
        return symbol;
    }

    public static RuleConditionValueType getInstance(int symbol) {
        for (RuleConditionValueType operator : RuleConditionValueType.values()) {
            if (operator.getSymbol() == symbol) {
                return operator;
            }
        }

        return UNKNOWN;
    }

}
