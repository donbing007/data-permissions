package com.xforceplus.ultraman.permissions.repository.entity;

import java.util.Objects;

/**
 * 搜索字段范围搜索.
 * @version 0.1 2019/11/5 16:59
 * @auth dongbin
 * @since 1.8
 */
public class SelectFieldScopeExample {

    private String roleId;

    private String tenantId;

    private String entity;

    public SelectFieldScopeExample() {
    }

    public SelectFieldScopeExample(String roleId, String tenantId, String entity) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SelectFieldScopeExample)) return false;
        SelectFieldScopeExample that = (SelectFieldScopeExample) o;
        return Objects.equals(getRoleId(), that.getRoleId()) &&
            Objects.equals(getTenantId(), that.getTenantId()) &&
            Objects.equals(getEntity(), that.getEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(), getTenantId(), getEntity());
    }

    @Override
    public String toString() {
        return "SelectColumnScopeByExample{" +
            "roleId='" + roleId + '\'' +
            ", tenantId='" + tenantId + '\'' +
            ", entity='" + entity + '\'' +
            '}';
    }
}
