package com.xforceplus.ultraman.permissions.service;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.page.Continuation;
import com.xforceplus.ultraman.permissions.pojo.result.service.DataRuleManagementResult;
import com.xforceplus.ultraman.permissions.pojo.result.service.FieldRuleManagementResult;
import com.xforceplus.ultraman.permissions.pojo.rule.DataRule;

import java.util.List;

/**
 * @version 0.1 2019/11/21 16:13
 * @author dongbin
 * @since 1.8
 */
public interface RuleDataRuleManagementService {

    /**
     * 创建或者更新一个数据规则.如果规则中的子列表变动,将进行同步.
     * @param authorization 授权信息.
     * @param rule 规则.
     * @return 结果.
     */
    DataRuleManagementResult save(Authorization authorization, DataRule rule);

    /**
     * 删除一个数据规则.
     * @param authorization 授权信息.
     * @param rule 规则.
     */
    DataRuleManagementResult remove(Authorization authorization, DataRule rule);

    /**
     * 迭代指定授权的所有数据规则.
     * @param authorization 授权信息.
     * @param continuation 继续信息.
     * @return 规则迭代器.
     */
    DataRuleManagementResult list(Authorization authorization, Continuation continuation);

    /**
     * 迭代指定授权的指定 entity 数据规则.
     * @param authorization 授权信息.
     * @param entity 实体信息.
     * @param continuation 继续信息.
     * @return
     */
    DataRuleManagementResult list(Authorization authorization, String entity, Continuation continuation);
}
