package com.xforceplus.ultraman.perissions.pojo.rule;

import java.io.Serializable;
import java.util.Objects;

/**
 * 某个 entity 中的某个 field 的规则条件.
 *
 * @version 0.1 2019/11/6 15:46
 * @auth dongbin
 * @since 1.8
 */
public class DataRuleCondition implements Serializable {
    private RuleConditionOperation operation;
    private RuleConditionValueType type;
    private RuleConditionRelationship link;
    private String value;

    public DataRuleCondition() {
    }

    public DataRuleCondition(
        RuleConditionOperation operation,
        RuleConditionValueType type,
        RuleConditionRelationship link,
        String value) {
        this.operation = operation;
        this.type = type;
        this.link = link;
        this.value = value;
    }

    public RuleConditionOperation getOperation() {
        return operation;
    }

    public void setOperation(RuleConditionOperation operation) {
        this.operation = operation;
    }

    public RuleConditionValueType getType() {
        return type;
    }

    public void setType(RuleConditionValueType type) {
        this.type = type;
    }

    public RuleConditionRelationship getLink() {
        return link;
    }

    public void setLink(RuleConditionRelationship link) {
        this.link = link;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataRuleCondition)) return false;
        DataRuleCondition that = (DataRuleCondition) o;
        return getOperation() == that.getOperation() &&
            getType() == that.getType() &&
            getLink() == that.getLink() &&
            Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOperation(), getType(), getLink(), getValue());
    }

    @Override
    public String toString() {
        return "DataRuleCondition{" +
            "operation=" + operation +
            ", type=" + type +
            ", link=" + link +
            ", value='" + value + '\'' +
            '}';
    }
}
