package com.xforceplus.ultraman.permissions.pojo.auth;

import java.io.Serializable;
import java.util.Objects;

/**
 * 表示授权信息.
 * @version 0.1 2019/11/6 15:30
 * @author dongbin
 * @since 1.8
 */
public class Authorization implements Serializable {
    private Long id;
    private String role;
    private String tenant;

    public Authorization() {
    }

    public Authorization(Long id, String role, String tenant) {
        this.id = id;
        this.role = role;
        this.tenant = tenant;
    }

    public Authorization(String role, String tenant) {
        this(null, role, tenant);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Authorization)) return false;
        Authorization authorization = (Authorization) o;
        return Objects.equals(getRole(), authorization.getRole()) &&
            Objects.equals(getTenant(), authorization.getTenant());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRole(), getTenant());
    }

    @Override
    public String toString() {
        return "Auth{" +
            "role='" + role + '\'' +
            ", tenant='" + tenant + '\'' +
            '}';
    }
}
