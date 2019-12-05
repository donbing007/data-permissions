package com.xforceplus.ultraman.permissions.pojo.rule;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

/**
 * 表示一个角色对于某个 entity 的某个字段.
 * @version 0.1 2019/11/6 15:42
 * @author dongbin
 * @since 1.8
 */

public class FieldRule implements Serializable {

    public static final byte TYPE = 0;

    private Long id;
    @NotEmpty(message = "The entity of a field rule cannot be null.")
    private String entity;

    @NotEmpty(message = "Invalid field name..")
    private String field;

    public FieldRule() {
    }

    public FieldRule(String entity, String field) {
        this.entity = entity;
        this.field = field;
    }

    public FieldRule(Long id, String entity, String field) {
        this.id = id;
        this.entity = entity;
        this.field = field;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldRule)) {
            return false;
        }
        FieldRule fieldRule = (FieldRule) o;
        return Objects.equals(getId(), fieldRule.getId()) &&
            Objects.equals(getEntity(), fieldRule.getEntity()) &&
            Objects.equals(getField(), fieldRule.getField());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEntity(), getField());
    }

    @Override
    public String toString() {
        return "FieldRule{" +
            "id=" + id +
            ", entity='" + entity + '\'' +
            ", field='" + field + '\'' +
            '}';
    }
}
