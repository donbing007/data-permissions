package com.xforceplus.ultraman.permissions.starter.authorization.impl;

import com.xforceplus.ultraman.perissions.pojo.Authorization;
import com.xforceplus.ultraman.permissions.starter.authorization.AuthorizationSearcher;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @version 0.1 2019/11/18 13:52
 * @auth dongbin
 * @since 1.8
 */
public class MockAuthorizationSearcher implements AuthorizationSearcher {

    private Authorization authorization;

    @Override
    public Authorization search() {
        return authorization;
    }

    public void set(String role, String tenant) {
        this.authorization = new Authorization(role, tenant);
    }
}
