package com.xforceplus.ultraman.perissions.pojo.rule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author dongbin
 * @version 0.1 2019/11/6 15:45
 * @since 1.8
 */
public class DataRule implements Serializable {

    private String entity;
    private String field;
    private List<DataRuleCondition> conditions;

    public DataRule() {
    }

    public DataRule(String entity, String field) {
        this(entity, field, null);
    }

    public DataRule(String entity, String field, List<DataRuleCondition> conditions) {
        this.entity = entity;
        this.field = field;
        this.conditions = conditions;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<DataRuleCondition> getConditions() {
        if (conditions == null) {
            return Collections.emptyList();
        } else {
            return conditions;
        }
    }

    public void addDataRuleCondition(DataRuleCondition dataRuleCondition) {
        if (conditions == null) {
            conditions = new ArrayList();
        }

        conditions.add(dataRuleCondition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataRule)) return false;
        DataRule dataRule = (DataRule) o;
        return Objects.equals(getEntity(), dataRule.getEntity()) &&
            Objects.equals(getField(), dataRule.getField()) &&
            Objects.equals(getConditions(), dataRule.getConditions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntity(), getField(), getConditions());
    }

    @Override
    public String toString() {
        return "DataRule{" +
            "entity='" + entity + '\'' +
            ", field='" + field + '\'' +
            ", conditions=" + conditions +
            '}';
    }
}
