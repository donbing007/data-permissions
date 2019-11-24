package com.xforceplus.ultraman.permissions.service;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.page.Continuation;
import com.xforceplus.ultraman.permissions.pojo.result.service.AuthorizationManagementResult;
import com.xforceplus.ultraman.permissions.pojo.result.service.FieldRuleManagementResult;

import java.util.Set;

/**
 * 授权信息管理.
 * @version 0.1 2019/11/21 17:21
 * @author dongbin
 * @since 1.8
 */
public interface RuleAuthorizationManagementService {

    /**
     * 保存授权信息.
     * @param authorization 目标授权信息.
     * @return 保存结果.
     */
    AuthorizationManagementResult save(Authorization authorization);

    /**
     * 列出授权信息.
     * @param continuation
     * @return 列表.
     */
    AuthorizationManagementResult list(Continuation continuation);
}
