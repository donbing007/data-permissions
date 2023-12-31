package com.xforceplus.ultraman.permissions.rule.convert.condition.value;

import com.xforceplus.ultraman.permissions.pojo.rule.DataRuleCondition;
import com.xforceplus.ultraman.permissions.pojo.rule.RuleConditionValueType;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.values.LongValue;

/**
 * @author dongbin
 * @version 0.1 2019/11/11 17:13
 * @since 1.8
 */
public class IntegerConditionValueConverting extends ConditionValueConverting {

    @Override
    public RuleConditionValueType support() {
        return RuleConditionValueType.INTEGER;
    }

    @Override
    protected Item doConvert(String value, DataRuleCondition rule) {
        return new LongValue(Long.parseLong(value));
    }

}
