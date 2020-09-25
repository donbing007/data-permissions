package com.xforceplus.ultraman.permissions.service.impl;

import com.github.pagehelper.PageHelper;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.page.Continuation;
import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.DataRuleManagementResult;
import com.xforceplus.ultraman.permissions.pojo.result.service.DataRuleManagementResultV2;
import com.xforceplus.ultraman.permissions.pojo.rule.*;
import com.xforceplus.ultraman.permissions.repository.DataScopeConditionsRepository;
import com.xforceplus.ultraman.permissions.repository.DataScopeRepository;
import com.xforceplus.ultraman.permissions.repository.DataScopeSubConditionRepository;
import com.xforceplus.ultraman.permissions.repository.RolePermissionsRepository;
import com.xforceplus.ultraman.permissions.repository.entity.*;
import com.xforceplus.ultraman.permissions.service.RuleDataRuleManagementService;
import com.xforceplus.ultraman.permissions.service.aop.AuthorizationCheck;
import com.xforceplus.ultraman.permissions.service.aop.NoAuthorizationPlan;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author dongbin
 * @version 0.1 2019/11/22 10:10
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


    @Resource
    private Validator validator;

    @Override
    @Transactional
    @AuthorizationCheck(NoAuthorizationPlan.CREATE)
    @CacheEvict(keyGenerator = "ruleSearchKeyGenerator", condition = "#rule.entity != null")
    public DataRuleManagementResult save(Authorization authorization, DataRule rule) {
        Set<ConstraintViolation<DataRule>> violationSet = validator.validate(rule);
        if (!violationSet.isEmpty()) {
            throw new IllegalArgumentException(violationSet.stream().findFirst().get().getMessage());
        }

        if (rule.getConditions() == null || rule.getConditions().isEmpty()) {
            throw new IllegalArgumentException("Invalid condition.");
        }

        DataScopeExample dataScopeExample = new DataScopeExample();
        dataScopeExample.createCriteria()
                .andEntityEqualTo(rule.getEntity())
                .andRoleEqualTo(authorization.getRole())
                .andTenantEqualTo(authorization.getTenant());
        List<DataScope> dataScopes = dataScopeRepository.selectByExample(dataScopeExample);

        DataScope dataScope;
        DataScopeConditions conditions;

        if (dataScopes.size() == 0) {
            // 没有创建过 entity
            dataScope = new DataScope();
            dataScope.setRole(authorization.getRole());
            dataScope.setTenant(authorization.getTenant());
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
        rule.setId(conditions.getId());
        createSubConditions(authorization, rule, conditions);
        return new DataRuleManagementResult(ManagementStatus.SUCCESS,rule);
    }

    @Override
    @Transactional
    @AuthorizationCheck(NoAuthorizationPlan.ERROR)
    @CacheEvict(keyGenerator = "ruleSearchKeyGenerator", condition = "#rule.entity != null")
    public DataRuleManagementResult remove(Authorization authorization, DataRule rule) {
        Set<ConstraintViolation<DataRule>> violationSet = validator.validate(rule);
        if (!violationSet.isEmpty()) {
            throw new IllegalArgumentException(violationSet.stream().findFirst().get().getMessage());
        }

        DataScopeSubConditionExample dataScopeSubConditionExample = new DataScopeSubConditionExample();
        dataScopeSubConditionExample.createCriteria().andConditionsIdEqualTo(rule.getId());

        if (dataScopeSubConditionRepository.deleteByExample(dataScopeSubConditionExample) > 0) {
            if (dataScopeConditionsRepository.deleteByPrimaryKey(rule.getId()) > 0) {
                return new DataRuleManagementResult(ManagementStatus.SUCCESS);
            }
        }

        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return new DataRuleManagementResult(ManagementStatus.LOSS, "Unable to remove data rule.");

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
        List<DataScopeSubCondition> dataScopeSubConditions = dataScopeSubConditionRepository.selectByExampleWithBLOBs(example);

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

    @Override
    @AuthorizationCheck(NoAuthorizationPlan.PASS)
    public DataRuleManagementResultV2 listV2(Authorization authorization, String entity) {

        DataScopeExample example = new DataScopeExample();
        DataScopeExample.Criteria criteria = example.createCriteria();
        criteria.andRoleEqualTo(authorization.getRole())
                .andTenantEqualTo(authorization.getTenant());
//        PageHelper.startPage(page, size);
        List<DataScope> dataScopes = dataScopeRepository.selectByExample(example);
        if(dataScopes.isEmpty()) {
            return new DataRuleManagementResultV2(ManagementStatus.SUCCESS, new ArrayList<>() , null);
        }
//        Long total = dataScopeRepository.countByExample(example);
        List<String> entities = dataScopes.stream().map(item -> item.getEntity()).collect(Collectors.toList());
        DataScopeSubConditionExample subExample = new DataScopeSubConditionExample();
        DataScopeSubConditionExample.Criteria subCriteria = subExample.createCriteria();
        subCriteria.andEntityIn(entities);
        subCriteria.andRoleEqualTo(authorization.getRole())
                .andTenantEqualTo(authorization.getTenant());
        List<DataScopeSubCondition> dataScopeSubConditions = dataScopeSubConditionRepository.selectByExampleWithBLOBs(subExample);
        Map<String, List<DataScopeSubCondition>> mapByEntity = dataScopeSubConditions.stream().collect(groupingBy(DataScopeSubCondition::getEntity));
        List<DataRuleV2> dataRuleV2s = new ArrayList<>();
        for (Map.Entry<String, List<DataScopeSubCondition>> entry : mapByEntity.entrySet()) {
            DataRuleV2 rule = new DataRuleV2();
            rule.setEntity(entry.getKey());
            Map<String, List<DataScopeSubCondition>> mapByField = entry.getValue().stream().collect(groupingBy(DataScopeSubCondition::getField));
            List<FieldAuthority> fieldAuthorities = new ArrayList<>();
            for (Map.Entry<String, List<DataScopeSubCondition>> fieldEntry : mapByField.entrySet()) {
                FieldAuthority fieldAuthority = new FieldAuthority();
                fieldAuthority.setName(fieldEntry.getKey());
                List<DataRuleCondition> conditions = fieldEntry.getValue().stream().map(item -> {
                    DataRuleCondition condition = new DataRuleCondition();
                    condition.setLink(RuleConditionRelationship.getInstance(item.getLink()));
                    condition.setOperation(RuleConditionOperation.getInstance(item.getOperation()));
                    condition.setType(RuleConditionValueType.getInstance(item.getValueType()));
                    condition.setValue(item.getValue());
                    return condition;
                }).collect(Collectors.toList());
                fieldAuthority.setConditions(conditions);
                fieldAuthority.setId(fieldEntry.getValue().size() != 0 ? fieldEntry.getValue().get(0).getConditionsId() : 0);
                fieldAuthorities.add(fieldAuthority);
            }
            rule.setFields(fieldAuthorities);
            dataRuleV2s.add(rule);
        }
        return new DataRuleManagementResultV2(ManagementStatus.SUCCESS, dataRuleV2s, null);
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
