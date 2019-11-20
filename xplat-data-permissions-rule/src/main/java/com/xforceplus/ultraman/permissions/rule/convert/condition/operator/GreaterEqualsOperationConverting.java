package com.xforceplus.ultraman.permissions.rule.convert.condition.operator;

import com.xforceplus.ultraman.permissions.pojo.rule.RuleConditionOperation;
import com.xforceplus.ultraman.permissions.sql.define.ConditionOperator;

/**
 * @author dongbin
 * @version 0.1 2019/11/11 18:47
 * @since 1.8
 */
public class GreaterEqualsOperationConverting extends ConditionOperationConverting {
    @Override
    public ConditionOperator convert(RuleConditionOperation value) {
        return ConditionOperator.GREATER_THAN_EQUALS;
    }
}
