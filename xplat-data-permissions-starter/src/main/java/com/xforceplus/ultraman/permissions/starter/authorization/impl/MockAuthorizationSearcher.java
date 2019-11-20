package com.xforceplus.ultraman.permissions.starter.authorization.impl;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.starter.authorization.AuthorizationSearcher;

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
