package com.xforceplus.ultraman.permissions.service.impl;

import com.github.pagehelper.PageHelper;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.page.Continuation;
import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.DataRuleManagementResult;
import com.xforceplus.ultraman.permissions.pojo.result.service.FieldRuleManagementResult;
import com.xforceplus.ultraman.permissions.pojo.rule.DataRule;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;
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

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
