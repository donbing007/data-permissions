package com.xforceplus.ultraman.permissions.jdbc.proxy;

import com.xforceplus.ultraman.permissions.jdbc.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;

/**
 * @author dongbin
 * @version 0.1 2019/11/19 10:22
 * @since 1.8
 */
public class AbstractStatementProxy {
    private RuleCheckServiceClient client;
    private Authorizations authorizations;

    public AbstractStatementProxy(RuleCheckServiceClient client, Authorizations authorizations) {
        this.client = client;
        this.authorizations = authorizations;
    }

    public RuleCheckServiceClient getClient() {
        return client;
    }

    public Authorizations getAuthorization() {
        return authorizations;
    }
}
