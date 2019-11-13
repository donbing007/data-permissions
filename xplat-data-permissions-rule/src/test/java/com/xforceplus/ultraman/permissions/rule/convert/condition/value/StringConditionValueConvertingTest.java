package com.xforceplus.ultraman.permissions.rule.convert.condition.value;

import com.xforceplus.ultraman.perissions.pojo.rule.DataRuleCondition;
import com.xforceplus.ultraman.perissions.pojo.rule.RuleConditionOperation;
import com.xforceplus.ultraman.perissions.pojo.rule.RuleConditionRelationship;
import com.xforceplus.ultraman.perissions.pojo.rule.RuleConditionValueType;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class StringConditionValueConvertingTest {

    @Test
    public void doConvert() {
        Map<DataRuleCondition, String> caseData = buildCase();

        StringConditionValueConverting converting = new StringConditionValueConverting();
        caseData.keySet().stream().forEach(c -> {
            Assert.assertEquals(caseData.get(c), converting.convert(c).get(0).toString());
        });
    }

    private Map<DataRuleCondition, String> buildCase() {

        Map<DataRuleCondition, String> data = new LinkedHashMap<>();

        data.put(
            new DataRuleCondition(
                RuleConditionOperation.CONTAINS,
                RuleConditionValueType.STRING,
                RuleConditionRelationship.AND,
                "value"),
            "%value%"
        );

        data.put(
            new DataRuleCondition(
                RuleConditionOperation.AFTER,
                RuleConditionValueType.STRING,
                RuleConditionRelationship.AND,
                "value"),
            "%value"
        );

        data.put(
            new DataRuleCondition(
                RuleConditionOperation.NOT_AFTER,
                RuleConditionValueType.STRING,
                RuleConditionRelationship.AND,
                "value"),
            "%value"
        );

        data.put(
            new DataRuleCondition(
                RuleConditionOperation.BEFORE,
                RuleConditionValueType.STRING,
                RuleConditionRelationship.AND,
                "value"),
            "value%"
        );

        data.put(
            new DataRuleCondition(
                RuleConditionOperation.NOT_BEFORE,
                RuleConditionValueType.STRING,
                RuleConditionRelationship.AND,
                "value"),
            "value%"
        );

        return data;
    }
}
