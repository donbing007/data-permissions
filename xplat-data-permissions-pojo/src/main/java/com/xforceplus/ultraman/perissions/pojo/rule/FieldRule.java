package com.xforceplus.ultraman.perissions.pojo.rule;

import java.io.Serializable;
import java.util.Objects;

/**
 * 表示一个角色对于某个 entity 的某个字段.
 * @version 0.1 2019/11/6 15:42
 * @auth dongbin
 * @since 1.8
 */
public class FieldRule implements Serializable {

    private String entity;
    private String field;

    public FieldRule() {
    }

    public FieldRule(String entity, String field) {
        this.entity = entity;
        this.field = field;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldRule)) return false;
        FieldRule fieldRule = (FieldRule) o;
        return Objects.equals(getEntity(), fieldRule.getEntity()) &&
            Objects.equals(getField(), fieldRule.getField());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntity(), getField());
    }

    @Override
    public String toString() {
        return "FieldRule{" +
            "entity='" + entity + '\'' +
            ", field='" + field + '\'' +
            '}';
    }
}
