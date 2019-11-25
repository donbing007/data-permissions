package com.xforceplus.ultraman.permissions.starter.client;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.pojo.result.service.CheckResult;

/**
 * 验证客户端.
 * @version 0.1 2019/11/15 14:24
 * @auth dongbin
 * @since 1.8
 */
public interface RuleCheckServiceClient {

    /**
     * 验证 SQL 在当前用户下的数据权限.
     * @param sql 原始 sql.
     * @param authorizations 授权信息.
     * @return 校验结果.
     */
    CheckResult check(String sql, Authorizations authorizations);
}
