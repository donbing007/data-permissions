package com.xforceplus.ultraman.permissions.starter.authorization.impl;

import com.xforceplus.ultraman.perissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.starter.authorization.AuthorizationSearcher;

/**
 * TODO: 还未实现从上下文中读取授权信息.
 * 从线程上下文中拿取授权信息.
 * @version 0.1 2019/11/19 15:38
 * @auth dongbin
 * @since 1.8
 */
public class ContextAuthorizationSearcher implements AuthorizationSearcher {

    @Override
    public Authorization search() {
        return null;
    }
}
