package com.xforceplus.ultraman.permissions.starter.jdbc.proxy;

import com.xforceplus.ultraman.perissions.pojo.Authorization;
import com.xforceplus.ultraman.permissions.starter.client.RuleCheckServiceClient;

/**
 * @version 0.1 2019/11/19 10:22
 * @auth dongbin
 * @since 1.8
 */
public class AbstractStatementProxy {
    private RuleCheckServiceClient client;
    private Authorization authorization;

    public AbstractStatementProxy(RuleCheckServiceClient client, Authorization authorization) {
        this.client = client;
        this.authorization = authorization;
    }

    public RuleCheckServiceClient getClient() {
        return client;
    }

    public Authorization getAuthorization() {
        return authorization;
    }
}
