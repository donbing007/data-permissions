package com.xforceplus.ultraman.permissions.jdbc.proxy;

import com.xforceplus.ultraman.permissions.jdbc.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.sql.hint.Hint;
import com.xforceplus.ultraman.permissions.sql.hint.parser.HintParser;

/**
 * 所有proxy 超类.
 * @author dongbin
 * @version 0.1 2019/11/19 10:22
 * @since 1.8
 */
public abstract class AbstractStatementProxy {

    private RuleCheckServiceClient client;
    private Authorizations authorizations;
    private HintParser hintParser;

    public AbstractStatementProxy(RuleCheckServiceClient client, Authorizations authorizations, HintParser hintParser) {
        this.client = client;
        this.authorizations = authorizations;
        this.hintParser = hintParser;
    }

    public RuleCheckServiceClient getClient() {
        return client;
    }

    public Authorizations getAuthorization() {
        return authorizations;
    }

    public HintParser getHintParser() {
        return hintParser;
    }
}
