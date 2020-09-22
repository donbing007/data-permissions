package com.xforceplus.ultraman.permissions.service;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.page.Continuation;
import com.xforceplus.ultraman.permissions.pojo.result.service.FieldRuleManagementResult;
import com.xforceplus.ultraman.permissions.pojo.result.service.FieldRuleManagementResultV2;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;

import java.util.List;

/**
 * 规则管理服务.
 * @version 0.1 2019/10/25 15:18
 * @author dongbin
 * @since 1.8
 */
public interface RuleFieldRuleManagementService {

    /**
     * 创建或者更新一个字段规则.
     * @param authorization 授权信息.
     * @param rule 规则信息.
     * @return 结果.
     */
    FieldRuleManagementResult save(Authorization authorization, FieldRule rule);

    /**
     * 删除已经存在的规则.
     * @param authorization 授权信息.
     * @param rule 规则.
     */
    FieldRuleManagementResult remove(Authorization authorization, FieldRule rule);

    /**
     * 迭代指定授权的字段规则.
     * @param authorization 授权信息.
     * @param continuation 继续信息.
     * @return 规则迭代器.
     */
    FieldRuleManagementResult list(Authorization authorization, Continuation continuation);

    /**
     * 迭代指定授权的指定 entity 规则 .
     * @param authorization
     * @param entity
     * @param continuation 继续信息.
     * @return 规则迭代器.
     */
    FieldRuleManagementResult list(Authorization authorization, String entity, Continuation continuation);


    /**
     * 迭代指定授权的指定 entity 规则 .
     * @param authorization
     * @param entity
     * @return 规则迭代器.
     */
    FieldRuleManagementResultV2 listV2(Authorization authorization, String entity);

}
