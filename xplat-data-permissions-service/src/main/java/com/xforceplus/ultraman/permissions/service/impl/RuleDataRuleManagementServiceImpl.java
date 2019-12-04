package com.xforceplus.ultraman.permissions.service.impl;

import com.github.pagehelper.PageHelper;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.page.Continuation;
import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.DataRuleManagementResult;
import com.xforceplus.ultraman.permissions.pojo.result.service.FieldRuleManagementResult;
import com.xforceplus.ultraman.permissions.pojo.rule.*;
import com.xforceplus.ultraman.permissions.repository.DataScopeConditionsRepository;
import com.xforceplus.ultraman.permissions.repository.DataScopeRepository;
import com.xforceplus.ultraman.permissions.repository.DataScopeSubConditionRepository;
import com.xforceplus.ultraman.permissions.repository.RolePermissionsRepository;
import com.xforceplus.ultraman.permissions.repository.entity.*;
import com.xforceplus.ultraman.permissions.service.RuleDataRuleManagementService;
import com.xforceplus.ultraman.permissions.service.aop.AuthorizationCheck;
import com.xforceplus.ultraman.permissions.service.aop.NoAuthorizationPlan;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 0.1 2019/11/22 10:10
 * @author dongbin
 * @since 1.8
 */
@CacheConfig(cacheNames = "rule")
@Service
public class RuleDataRuleManagementServiceImpl implements RuleDataRuleManagementService {

    @Resource
    private RolePermissionsRepository rolePermissionsRepository;

    @Resource
    private DataScopeRepository dataScopeRepository;

    @Resource
    private DataScopeConditionsRepository dataScopeConditionsRepository;

    @Resource
    private DataScopeSubConditionRepository dataScopeSubConditionRepository;


    @Override
    @Transactional
    @AuthorizationCheck(NoAuthorizationPlan.CREATE)
    @CacheEvict(keyGenerator = "ruleSearchKeyGenerator")
    public DataRuleManagementResult save(Authorization authorization, DataRule rule) {
        DataScopeExample dataScopeExample = new DataScopeExample();
        dataScopeExample.createCriteria().andEntityEqualTo(rule.getEntity());
        List<DataScope> dataScopes = dataScopeRepository.selectByExample(dataScopeExample);

        DataScope dataScope;
        DataScopeConditions conditions;

        if (dataScopes.size() == 0) {
            // 没有创建过 entity
            dataScope = new DataScope();
            dataScope.setEntity(rule.getEntity());
            dataScopeRepository.insert(dataScope);

            conditions = new DataScopeConditions();
            conditions.setDataScopeId(dataScope.getId());
            conditions.setField(rule.getField());

            dataScopeConditionsRepository.insert(conditions);

            RolePermissions rolePermissions = new RolePermissions();
            rolePermissions.setScopeType(DataRule.TYPE);
            rolePermissions.setScopeId(dataScope.getId());
            rolePermissions.setRoleId(authorization.getId());

            rolePermissionsRepository.insert(rolePermissions);


        } else {
            dataScope = dataScopes.get(0);
            /**
             * 表示 entity 已经存在,检查 field 是否存在,不存在创建,存在更新子条件.
             */

            DataScopeConditionsExample dataScopeConditionsExample = new DataScopeConditionsExample();
            dataScopeConditionsExample.createCriteria().andFieldEqualTo(rule.getField())
                .andDataScopeIdEqualTo(dataScopes.get(0).getId());

            List<DataScopeConditions> dataScopeConditions =
                dataScopeConditionsRepository.selectByExample(dataScopeConditionsExample);
            if (dataScopeConditions.size() > 0) {
                // 存在条件,删除之下的所有子条件,增加上新的条件.
                conditions = dataScopeConditions.get(0);

                DataScopeSubConditionExample dataScopeSubConditionExample = new DataScopeSubConditionExample();
                dataScopeSubConditionExample.createCriteria().andConditionsIdEqualTo(conditions.getId());
                dataScopeSubConditionRepository.deleteByExample(dataScopeSubConditionExample);

            } else {
                // 不存在此 field 条件,创建条件及其子条件.
                conditions = new DataScopeConditions();
                conditions.setField(rule.getField());
                conditions.setDataScopeId(dataScope.getId());
                dataScopeConditionsRepository.insert(conditions);

            }

        }

        createSubConditions(authorization, rule, conditions);

        return new DataRuleManagementResult(ManagementStatus.SUCCESS);
    }

    @Override
    @Transactional
    @AuthorizationCheck(NoAuthorizationPlan.ERROR)
    @CacheEvict(keyGenerator = "ruleSearchKeyGenerator")
    public DataRuleManagementResult remove(Authorization authorization, DataRule rule) {
        DataScopeSubConditionExample dataScopeSubConditionExample = new DataScopeSubConditionExample();
        dataScopeSubConditionExample.createCriteria().andEntityEqualTo(rule.getEntity())
            .andFieldEqualTo(rule.getField())
            .andRoleEqualTo(authorization.getRole())
            .andTenantEqualTo(authorization.getTenant());
        if (dataScopeSubConditionRepository.deleteByExample(dataScopeSubConditionExample) > 0) {
            if (dataScopeConditionsRepository.deleteByPrimaryKey(rule.getId()) > 0) {
                return new DataRuleManagementResult(ManagementStatus.SUCCESS);
            }
        }

        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return new DataRuleManagementResult(ManagementStatus.FAIL, "Unable to remove data rule.");

    }

    @Override
    @AuthorizationCheck(NoAuthorizationPlan.ERROR)
    public DataRuleManagementResult list(Authorization authorization, Continuation continuation) {
        return list(authorization, continuation);
    }

    @Override
    @AuthorizationCheck(NoAuthorizationPlan.ERROR)
    public DataRuleManagementResult list(Authorization authorization, String entity, Continuation continuation) {
        DataScopeSubConditionExample example = new DataScopeSubConditionExample();
        example.setOrderByClause("`id` asc, `conditions_id` asc, `index` asc");

        DataScopeSubConditionExample.Criteria criteria = example.createCriteria();
        if (entity != null) {
            criteria.andEntityEqualTo(entity);
        }
        criteria.andRoleEqualTo(authorization.getRole())
            .andTenantEqualTo(authorization.getTenant())
            .andIdGreaterThan(continuation.getStart());

        PageHelper.startPage(1, continuation.getSize());
        List<DataScopeSubCondition> dataScopeSubConditions = dataScopeSubConditionRepository.selectByExample(example);

        // field = rule
        Map<Long, DataRule> buff = new LinkedHashMap<>();
        DataRule rule;
        DataRuleCondition condition;
        for (DataScopeSubCondition dataScopeSubCondition : dataScopeSubConditions) {
            rule = buff.get(dataScopeSubCondition.getConditionsId());
            if (rule == null) {
                rule = new DataRule(
                    dataScopeSubCondition.getConditionsId(),
                    dataScopeSubCondition.getEntity(),
                    dataScopeSubCondition.getField());
                buff.put(dataScopeSubCondition.getConditionsId(), rule);
            }

            condition = new DataRuleCondition();
            condition.setLink(RuleConditionRelationship.getInstance(dataScopeSubCondition.getLink()));
            condition.setOperation(RuleConditionOperation.getInstance(dataScopeSubCondition.getOperation()));
            condition.setType(RuleConditionValueType.getInstance(dataScopeSubCondition.getValueType()));
            condition.setValue(dataScopeSubCondition.getValue());
            rule.addDataRuleCondition(condition);

        }

        return new DataRuleManagementResult(ManagementStatus.SUCCESS, buff.values(), null);

    }

    /**
     * 创建子条件.
     */
    private void createSubConditions(Authorization authorization, DataRule rule, DataScopeConditions conditions) {
        short index = 0;
        for (DataRuleCondition condition : rule.getConditions()) {

            DataScopeSubCondition subCondition = new DataScopeSubCondition();
            subCondition.setConditionsId(conditions.getId());
            subCondition.setEntity(rule.getEntity());
            subCondition.setField(rule.getField());
            subCondition.setIndex(index++);
            subCondition.setLink((byte) condition.getLink().getSymbol());
            subCondition.setOperation(condition.getOperation().getSymbol());
            subCondition.setValue(condition.getValue());
            subCondition.setValueType((byte) condition.getType().getSymbol());
            subCondition.setRole(authorization.getRole());
            subCondition.setTenant(authorization.getTenant());

            dataScopeSubConditionRepository.insert(subCondition);
        }
    }
}
