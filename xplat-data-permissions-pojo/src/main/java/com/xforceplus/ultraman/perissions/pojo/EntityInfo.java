package com.xforceplus.ultraman.perissions.pojo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @version 0.1 2019/11/6 18:25
 * @auth dongbin
 * @since 1.8
 */
public class EntityInfo implements Serializable {

    private String entity;
    private String field;

    public EntityInfo() {
    }

    public EntityInfo(String entity) {
        this(entity,null);
    }

    public EntityInfo(String entity, String field) {
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
        if (!(o instanceof EntityInfo)) return false;
        EntityInfo that = (EntityInfo) o;
        return Objects.equals(getEntity(), that.getEntity()) &&
            Objects.equals(getField(), that.getField());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntity(), getField());
    }

    @Override
    public String toString() {
        return field == null ? entity : entity + "." + field;
    }
}
