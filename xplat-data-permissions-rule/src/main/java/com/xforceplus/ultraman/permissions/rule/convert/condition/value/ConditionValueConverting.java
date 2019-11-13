package com.xforceplus.ultraman.permissions.rule.convert.condition.value;

import com.xforceplus.ultraman.perissions.pojo.rule.DataRuleCondition;
import com.xforceplus.ultraman.perissions.pojo.rule.RuleConditionValueType;
import com.xforceplus.ultraman.permissions.rule.convert.Converting;
import com.xforceplus.ultraman.permissions.rule.utils.ValueHelper;
import com.xforceplus.ultraman.permissions.sql.define.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version 0.1 2019/11/11 17:05
 * @auth dongbin
 * @since 1.8
 */
public abstract class ConditionValueConverting implements Converting<List<Item>, DataRuleCondition> {

    public abstract RuleConditionValueType support();

    @Override
    public List<Item> convert(DataRuleCondition rule) {

        if (support() != rule.getType()) {
            throw new IllegalArgumentException(
                String.format("Unsupported types, only %s types are supported.", support().getSymbol()));
        }

        if (isMultiple(rule)) {
            String[] values = ValueHelper.parserMultipleValue(rule.getValue());
            List<Item> items = new ArrayList<>();
            for (String v : values) {
                items.add(doConvert(v, rule));
            }

            return items;

        } else {
            return Arrays.asList(doConvert(rule.getValue(), rule));
        }
    }

    private boolean isMultiple(DataRuleCondition rule) {
        switch(rule.getOperation()) {
            case LIST: return true;
            default: return false;
        }
    }

    protected abstract Item doConvert(String value, DataRuleCondition rule);

}
