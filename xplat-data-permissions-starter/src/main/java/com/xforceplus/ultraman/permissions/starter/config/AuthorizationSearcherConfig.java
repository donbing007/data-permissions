package com.xforceplus.ultraman.permissions.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 授权信息搜索配置.
 * @version 0.1 2019/11/19 15:22
 * @auth dongbin
 * @since 1.8
 */
@ConfigurationProperties(prefix = "xplat.data.permissions.searcher")
@Component
public class AuthorizationSearcherConfig {

    private String name;

    private String role;

    private String tenant;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
