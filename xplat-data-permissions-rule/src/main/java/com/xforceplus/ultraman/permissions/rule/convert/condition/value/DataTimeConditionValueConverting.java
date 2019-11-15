package com.xforceplus.ultraman.permissions.rule.convert.condition.value;

import com.xforceplus.ultraman.perissions.pojo.rule.DataRuleCondition;
import com.xforceplus.ultraman.perissions.pojo.rule.RuleConditionValueType;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.values.StringValue;

/**
 * @author dongbin
 * @version 0.1 2019/11/11 18:34
 * @since 1.8
 */
public class DataTimeConditionValueConverting extends ConditionValueConverting {
    @Override
    public RuleConditionValueType support() {
        return RuleConditionValueType.DATATIME;
    }

    @Override
    protected Item doConvert(String value, DataRuleCondition rule) {
        return new StringValue(value);
    }
}
