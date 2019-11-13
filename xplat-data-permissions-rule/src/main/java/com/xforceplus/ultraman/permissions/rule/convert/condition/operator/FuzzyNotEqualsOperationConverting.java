package com.xforceplus.ultraman.permissions.rule.convert.condition.operator;

import com.xforceplus.ultraman.perissions.pojo.rule.RuleConditionOperation;
import com.xforceplus.ultraman.permissions.sql.define.ConditionOperator;

/**
 * @version 0.1 2019/11/11 18:56
 * @auth dongbin
 * @since 1.8
 */
public class FuzzyNotEqualsOperationConverting extends ConditionOperationConverting {
    @Override
    public ConditionOperator convert(RuleConditionOperation value) {
        return ConditionOperator.NOT_LIKE;
    }
}
