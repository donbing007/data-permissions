package com.xforceplus.ultraman.permissions.repository.entity;

import java.util.Objects;

/**
 * 搜索数据范围的条件.
 * @version 0.1 2019/11/5 17:32
 * @auth dongbin
 * @since 1.8
 */
public class SelectDataScopeExample {

    private String roleId;

    private String tenantId;

    private String entity;

    private String field;

    public SelectDataScopeExample() {
    }

    public SelectDataScopeExample(String roleId, String tenantId, String entity, String field) {
        this.roleId = roleId;
        this.tenantId = tenantId;
        this.entity = entity;
        this.field = field;
    }


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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
        if (!(o instanceof SelectDataScopeExample)) return false;
        SelectDataScopeExample that = (SelectDataScopeExample) o;
        return Objects.equals(getRoleId(), that.getRoleId()) &&
            Objects.equals(getTenantId(), that.getTenantId()) &&
            Objects.equals(getEntity(), that.getEntity()) &&
            Objects.equals(getField(), that.getField());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(), getTenantId(), getEntity(), getField());
    }

    @Override
    public String toString() {
        return "SelectDataScopeExample{" +
            "roleId='" + roleId + '\'' +
            ", tenantId='" + tenantId + '\'' +
            ", entity='" + entity + '\'' +
            ", field='" + field + '\'' +
            '}';
    }
}
