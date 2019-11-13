package com.xforceplus.ultraman.permissions.service;

import com.xforceplus.ultraman.perissions.pojo.Authorization;
import com.xforceplus.ultraman.perissions.pojo.result.service.CheckResult;

/**
 * 规则检查服务.
 * @version 0.1 2019/10/25 15:20
 * @auth dongbin
 * @since 1.8
 */
public interface RuleCheckService {

    /**
     * 检查 sql.
     * @param sql 目标 sql.
     * @param authorization 授权信息.
     * @return 检查结果.
     */
    CheckResult check(String sql, Authorization authorization);

}
