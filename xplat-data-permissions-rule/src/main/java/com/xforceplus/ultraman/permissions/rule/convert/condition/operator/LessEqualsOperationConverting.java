package com.xforceplus.ultraman.permissions.rule.convert.condition.operator;

import com.xforceplus.ultraman.perissions.pojo.rule.RuleConditionOperation;
import com.xforceplus.ultraman.permissions.sql.define.ConditionOperator;

/**
 * @author dongbin
 * @version 0.1 2019/11/11 18:47
 * @since 1.8
 */
public class LessEqualsOperationConverting extends ConditionOperationConverting {
    @Override
    public ConditionOperator convert(RuleConditionOperation value) {
        return ConditionOperator.MINOR_THAN_EQUALS;
    }
}
