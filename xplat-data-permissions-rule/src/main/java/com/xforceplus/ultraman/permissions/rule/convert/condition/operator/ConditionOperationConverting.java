package com.xforceplus.ultraman.permissions.rule.convert.condition.operator;

import com.xforceplus.ultraman.perissions.pojo.rule.RuleConditionOperation;
import com.xforceplus.ultraman.permissions.rule.convert.Converting;
import com.xforceplus.ultraman.permissions.sql.define.ConditionOperator;

/**
 * @version 0.1 2019/11/11 18:43
 * @auth dongbin
 * @since 1.8
 */
public abstract class ConditionOperationConverting implements Converting<ConditionOperator, RuleConditionOperation> {

    @Override
    public abstract ConditionOperator convert(RuleConditionOperation value);
}
