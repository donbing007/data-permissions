package com.xforceplus.ultraman.permissions.jdbc.authorization.impl;

import com.xforceplus.ultraman.permissions.jdbc.authorization.AuthorizationSearcher;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;

import java.util.Arrays;

/**
 * 基于测试的 mock 实现.
 * @version 0.1 2019/11/18 13:52
 * @auth dongbin
 * @since 1.8
 */
public class MockAuthorizationSearcher implements AuthorizationSearcher {

    private Authorizations authorizations;

    public MockAuthorizationSearcher(String role, String tenant) {
        this.authorizations = new Authorizations(Arrays.asList(new Authorization(role,tenant)));
    }

    @Override
    public Authorizations search() {
        return authorizations;
    }

    public void set(String role, String tenant) {
        this.authorizations = new Authorizations(Arrays.asList(new Authorization(role,tenant)));
    }

}
