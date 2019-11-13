package com.xforceplus.ultraman.permissions.rule.convert.condition.value;

import com.xforceplus.ultraman.perissions.pojo.rule.RuleConditionValueType;
import com.xforceplus.ultraman.permissions.rule.convert.Converting;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 0.1 2019/11/11 18:35
 * @auth dongbin
 * @since 1.8
 */
public class ConditionValueConvertingFactory {

    private static final Map<RuleConditionValueType, ConditionValueConverting> instances = new HashMap<>();

    static {
        instances.put(RuleConditionValueType.INTEGER, new IntegerConditionValueConverting());
        instances.put(RuleConditionValueType.STRING, new StringConditionValueConverting());
        instances.put(RuleConditionValueType.FLOAT, new FloatConditionValueConverting());
        instances.put(RuleConditionValueType.DATATIME, new DataTimeConditionValueConverting());
    }

    public static ConditionValueConverting getConverting(RuleConditionValueType type) {
        return instances.get(type);
    }
}
