package com.xforceplus.ultraman.permissions.rule.convert.condition.value;

import com.xforceplus.ultraman.permissions.pojo.rule.DataRuleCondition;
import com.xforceplus.ultraman.permissions.pojo.rule.RuleConditionValueType;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.values.DoubleValue;

/**
 * @author dongbin
 * @version 0.1 2019/11/11 18:32
 * @since 1.8
 */
public class FloatConditionValueConverting extends ConditionValueConverting {
    @Override
    public RuleConditionValueType support() {
        return RuleConditionValueType.FLOAT;
    }

    @Override
    protected Item doConvert(String value, DataRuleCondition rule) {
        return new DoubleValue(Double.parseDouble(value));
    }
}
