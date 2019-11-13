package com.xforceplus.ultraman.permissions.rule.convert.condition.value;

import com.xforceplus.ultraman.perissions.pojo.rule.DataRuleCondition;
import com.xforceplus.ultraman.perissions.pojo.rule.RuleConditionValueType;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.values.StringValue;

/**
 * @version 0.1 2019/11/11 18:32
 * @auth dongbin
 * @since 1.8
 */
public class StringConditionValueConverting extends ConditionValueConverting {
    @Override
    public RuleConditionValueType support() {
        return RuleConditionValueType.STRING;
    }

    @Override
    protected Item doConvert(String value, DataRuleCondition rule) {

        StringBuilder buff = new StringBuilder(calculateBufferLen(value));

        switch(rule.getOperation()) {
            case CONTAINS:  {
                buff.append('%').append(value).append('%');
                break;
            }
            case AFTER:
            case NOT_AFTER: {
                buff.append('%').append(value);
                break;
            }
            case BEFORE:
            case NOT_BEFORE: {
                buff.append(value).append('%');
                break;
            }
            default: buff.append(value);
        }

        return new StringValue(buff.toString());
    }

    private int calculateBufferLen(String value) {
        // 包含两边单引号和两边的'%'符号.
        final int fixLen = 4;
        return value.length() + fixLen;
    }
}
