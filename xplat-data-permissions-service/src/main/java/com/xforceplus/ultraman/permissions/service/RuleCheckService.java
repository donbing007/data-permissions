package com.xforceplus.ultraman.permissions.service;

import com.xforceplus.ultraman.perissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.perissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.perissions.pojo.result.service.CheckResult;

/**
 * 规则检查服务.
 * @version 0.1 2019/10/25 15:20
 * @author dongbin
 * @since 1.8
 */
public interface RuleCheckService {

    /**
     * 检查 sql.
     * @param sql 目标 sql.
     * @param authorizations 授权信息.
     * @return 检查结果.
     */
    CheckResult check(String sql, Authorizations authorizations);

}
