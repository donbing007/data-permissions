package com.xforceplus.ultraman.permissions.service.impl;

import com.github.pagehelper.PageHelper;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.page.Continuation;
import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.FieldRuleManagementResult;
import com.xforceplus.ultraman.permissions.pojo.result.service.FieldRuleManagementResultV2;
import com.xforceplus.ultraman.permissions.pojo.rule.DataRuleV2;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldAuthority;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRuleRequest;
import com.xforceplus.ultraman.permissions.repository.FieldScopeRepository;
import com.xforceplus.ultraman.permissions.repository.RolePermissionsRepository;
import com.xforceplus.ultraman.permissions.repository.entity.FieldScope;
import com.xforceplus.ultraman.permissions.repository.entity.FieldScopeExample;
import com.xforceplus.ultraman.permissions.repository.entity.RolePermissions;
import com.xforceplus.ultraman.permissions.repository.entity.RolePermissionsExample;
import com.xforceplus.ultraman.permissions.service.RuleFieldRuleManagementService;
import com.xforceplus.ultraman.permissions.service.aop.AuthorizationCheck;
import com.xforceplus.ultraman.permissions.service.aop.NoAuthorizationPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author dongbin
 * @version 0.1 2019/11/21 12:36
 * @since 1.8
 */
@Service
@CacheConfig(cacheNames = "rule")
public class RuleFieldRuleManagementServiceImpl implements RuleFieldRuleManagementService {

    final Logger logger = LoggerFactory.getLogger(RuleFieldRuleManagementServiceImpl.class);

    @Resource
    private RolePermissionsRepository rolePermissionsRepository;

    @Resource
    private FieldScopeRepository fieldScopeRepository;

    @Resource
    private Validator validator;

    @Override
    @Transactional
    @AuthorizationCheck(NoAuthorizationPlan.CREATE)
    @CacheEvict(keyGenerator = "ruleSearchKeyGenerator", condition = "#rule.entity != null")
    public FieldRuleManagementResult save(Authorization authorization, FieldRule rule) {
        Set<ConstraintViolation<FieldRule>> violationSet = validator.validate(rule);
        if (!violationSet.isEmpty()) {
            throw new IllegalArgumentException(violationSet.stream().findFirst().get().getMessage());
        }

        if (isExist(authorization, rule)) {
            return new FieldRuleManagementResult(ManagementStatus.REPETITION);
        }


        if (rule.getId() == null) {
            // create
            FieldScope scope = new FieldScope();
            scope.setEntity(rule.getEntity());
            scope.setField(rule.getField());
            scope.setRole(authorization.getRole());
            scope.setTenant(authorization.getTenant());
            if (fieldScopeRepository.insert(scope) > 0) {

                RolePermissions rolePermissions = new RolePermissions();
                rolePermissions.setRoleId(authorization.getId());
                rolePermissions.setScopeId(scope.getId());
                rolePermissions.setScopeType(FieldRule.TYPE);
                if (rolePermissionsRepository.insert(rolePermissions) > 0) {

                    return new FieldRuleManagementResult(ManagementStatus.SUCCESS);

                }
            }

        } else {
            // update

            FieldScope scope = fieldScopeRepository.selectByPrimaryKey(rule.getId());
            if (scope == null) {

                return new FieldRuleManagementResult(ManagementStatus.LOSS);

            } else {

                scope.setEntity(rule.getEntity());
                scope.setField(rule.getField());

                if (fieldScopeRepository.updateByPrimaryKey(scope) > 0) {
                    return new FieldRuleManagementResult(ManagementStatus.SUCCESS);
                }
            }

        }

        return new FieldRuleManagementResult(ManagementStatus.FAIL);
    }

    @Override
    public FieldRuleManagementResult insert(Authorization authorization, FieldRuleRequest rules) {
        if(StringUtils.isEmpty(rules.getEntity()) || rules.getFields().isEmpty()) {
            throw new IllegalArgumentException("field or entity is empty!");
        }
        for(String field : rules.getFields()) {
            FieldScope scope = new FieldScope();
            scope.setEntity(rules.getEntity());
            scope.setField(field);
            scope.setRole(authorization.getRole());
            scope.setTenant(authorization.getTenant());
            fieldScopeRepository.insert(scope);
            RolePermissions rolePermissions = new RolePermissions();
            rolePermissions.setRoleId(authorization.getId());
            rolePermissions.setScopeId(scope.getId());
            rolePermissions.setScopeType(FieldRule.TYPE);
            rolePermissionsRepository.insert(rolePermissions);
        }
        return new FieldRuleManagementResult(ManagementStatus.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @AuthorizationCheck(NoAuthorizationPlan.ERROR)
    public FieldRuleManagementResult update(Authorization authorization, FieldRuleRequest ruleRequest) {
        if(StringUtils.isEmpty(ruleRequest.getEntity()) || ruleRequest.getFields().isEmpty()) {
            throw new IllegalArgumentException("field or entity is empty!");
        }
        removeBatch(authorization,ruleRequest.getEntity());
        insert(authorization,ruleRequest);
        return new FieldRuleManagementResult(ManagementStatus.SUCCESS);
    }

    @Override
    @Transactional
    @AuthorizationCheck(NoAuthorizationPlan.ERROR)
    @CacheEvict(keyGenerator = "ruleSearchKeyGenerator", condition = "#rule.entity != null")
    public FieldRuleManagementResult remove(Authorization authorization, FieldRule rule) {
        Set<ConstraintViolation<FieldRule>> violationSet = validator.validate(rule);
        if (!violationSet.isEmpty()) {
            throw new IllegalArgumentException(violationSet.stream().findFirst().get().getMessage());
        }

        RolePermissionsExample example = new RolePermissionsExample();
        example.createCriteria().andRoleIdEqualTo(authorization.getId())
            .andScopeIdEqualTo(rule.getId())
            .andScopeTypeEqualTo(FieldRule.TYPE);
        if (rolePermissionsRepository.deleteByExample(example) > 0) {

            if (fieldScopeRepository.deleteByPrimaryKey(rule.getId()) > 0) {

                return new FieldRuleManagementResult(ManagementStatus.SUCCESS);

            }
        }

        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return new FieldRuleManagementResult(ManagementStatus.LOSS, "Unable to remove field rule.");
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @AuthorizationCheck(NoAuthorizationPlan.ERROR)
    public FieldRuleManagementResult removeBatch(Authorization authorization, String entity) {
        FieldScopeExample example = new FieldScopeExample();
        example.createCriteria().andEntityEqualTo(entity)
                .andTenantEqualTo(authorization.getTenant())
                .andRoleEqualTo(authorization.getRole());
        List<FieldScope> fieldScopes =  fieldScopeRepository.selectByExample(example);
        RolePermissionsExample roleExample = new RolePermissionsExample();
        List<Long> fieldScopeIds = fieldScopes.stream().map(item->item.getId()).collect(Collectors.toList());
        logger.info("prepare delete fieldScopeIds {}",fieldScopeIds.size());
        roleExample.createCriteria().andRoleIdEqualTo(authorization.getId())
                .andScopeIdIn(fieldScopeIds)
                .andScopeTypeEqualTo(FieldRule.TYPE);
        if (rolePermissionsRepository.deleteByExample(roleExample) > 0) {

            FieldScopeExample delExample = new FieldScopeExample();
            delExample.createCriteria().andIdIn(fieldScopeIds);
            if (fieldScopeRepository.deleteByExample(delExample) > 0) {

                return new FieldRuleManagementResult(ManagementStatus.SUCCESS);
            }
        }
        return null;
    }

    @Override
    @AuthorizationCheck(NoAuthorizationPlan.ERROR)
    public FieldRuleManagementResult list(Authorization authorization, Continuation continuation) {
        return list(authorization, null, continuation);
    }

    @Override
    @AuthorizationCheck(NoAuthorizationPlan.ERROR)
    public FieldRuleManagementResult list(Authorization authorization, String entity, Continuation continuation) {
        FieldScopeExample example = new FieldScopeExample();
        example.setOrderByClause("id ASC");
        FieldScopeExample.Criteria criteria = example.createCriteria();
        criteria.andRoleEqualTo(authorization.getRole())
            .andTenantEqualTo(authorization.getTenant())
            .andIdGreaterThan(continuation.getStart());
        if (entity != null) {
            criteria.andEntityEqualTo(entity);
        }

        PageHelper.startPage(1, continuation.getSize());
        List<FieldScope> scopes = fieldScopeRepository.selectByExample(example);

        List<FieldRule> rules = scopes.stream()
            .map(scope -> new FieldRule(scope.getId(), scope.getEntity(), scope.getField()))
            .collect(Collectors.toList());

        return new FieldRuleManagementResult(ManagementStatus.SUCCESS, rules, null);
    }

    @Override
    @AuthorizationCheck(NoAuthorizationPlan.ERROR)
    public FieldRuleManagementResultV2 listV2(Authorization authorization, String entity) {
        FieldScopeExample example = new FieldScopeExample();
        example.setOrderByClause("id ASC");
        FieldScopeExample.Criteria criteria = example.createCriteria();
        criteria.andRoleEqualTo(authorization.getRole())
            .andTenantEqualTo(authorization.getTenant());
        if (entity != null) {
            criteria.andEntityEqualTo(entity);
        }
        List<FieldScope> scopes = fieldScopeRepository.selectByExample(example);
       Map<String,List<FieldScope>> mapScopes =  scopes.stream().collect(groupingBy(FieldScope::getEntity));
       List<DataRuleV2> result = new ArrayList<>();
       for(Map.Entry<String,List<FieldScope>> entry : mapScopes.entrySet()) {
           DataRuleV2 ruleV2 = new DataRuleV2();
           ruleV2.setEntity(entry.getKey());
           List<FieldAuthority> fieldAuthorities = entry.getValue().stream().map(item->{
               FieldAuthority fieldAuthority = new FieldAuthority();
               fieldAuthority.setId(item.getId());
               fieldAuthority.setName(item.getField());
               return fieldAuthority;
           }).collect(Collectors.toList());
           ruleV2.setFields(fieldAuthorities);
           result.add(ruleV2);
       }
        return new FieldRuleManagementResultV2(ManagementStatus.SUCCESS, result, null);
    }

    /**
     * true 表示已经存在,false 不存在.
     */
    private boolean isExist(Authorization authorization, FieldRule rule) {
        FieldScopeExample example = new FieldScopeExample();
        example.createCriteria().andRoleEqualTo(authorization.getRole())
            .andTenantEqualTo(authorization.getTenant())
            .andEntityEqualTo(rule.getEntity())
            .andFieldEqualTo(rule.getField());
        return fieldScopeRepository.countByExample(example) > 0;
    }
}
