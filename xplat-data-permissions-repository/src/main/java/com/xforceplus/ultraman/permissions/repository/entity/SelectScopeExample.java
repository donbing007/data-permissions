package com.xforceplus.ultraman.permissions.repository.entity;

import java.util.Objects;

/**
 * 搜索字段范围搜索.
 * @version 0.1 2019/11/5 16:59
 * @author dongbin
 * @since 1.8
 */
public class SelectScopeExample {

    private String roleId;

    private String tenantId;

    private String entity;

    private String field;

    public SelectScopeExample() {
    }

    public SelectScopeExample(String roleId, String tenantId, String entity, String field) {
        this.roleId = roleId;
        this.tenantId = tenantId;
        this.entity = entity;
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
        if (!(o instanceof SelectScopeExample)) return false;
        SelectScopeExample that = (SelectScopeExample) o;
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
        return "SelectScopeExample{" +
            "roleId='" + roleId + '\'' +
            ", tenantId='" + tenantId + '\'' +
            ", entity='" + entity + '\'' +
            ", field='" + field + '\'' +
            '}';
    }
}
