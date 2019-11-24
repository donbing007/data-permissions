package com.xforceplus.ultraman.permissions.service.impl;

import com.github.pagehelper.PageHelper;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.page.Continuation;
import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.AuthorizationManagementResult;
import com.xforceplus.ultraman.permissions.pojo.result.service.FieldRuleManagementResult;
import com.xforceplus.ultraman.permissions.repository.RoleRepository;
import com.xforceplus.ultraman.permissions.repository.entity.Role;
import com.xforceplus.ultraman.permissions.repository.entity.RoleExample;
import com.xforceplus.ultraman.permissions.service.RuleAuthorizationManagementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @version 0.1 2019/11/21 17:25
 * @author dongbin
 * @since 1.8
 */
@Service
public class RuleAuthorizationManagementServiceImpl implements RuleAuthorizationManagementService {

    @Resource
    private RoleRepository roleRepository;

    @Override
    public AuthorizationManagementResult save(Authorization authorization) {
        RoleExample example = new RoleExample();
        example.createCriteria().andRoleExternalIdEqualTo(authorization.getRole())
            .andTenantIdEqualTo(authorization.getTenant());
        List<Role> roles = roleRepository.selectByExample(example);

        if (roles.isEmpty()) {

            Role newRole = new Role();
            newRole.setRoleExternalId(authorization.getRole());
            newRole.setTenantId(authorization.getTenant());
            if (roleRepository.insert(newRole) > 0) {

                return new AuthorizationManagementResult(ManagementStatus.SUCCESS);

            } else {

                return new AuthorizationManagementResult(ManagementStatus.FAIL);

            }

        } else {

            return new AuthorizationManagementResult(ManagementStatus.SUCCESS);
        }

    }

    @Override
    public AuthorizationManagementResult list(Continuation continuation) {


        RoleExample example = new RoleExample();
        example.createCriteria().andIdGreaterThan(continuation.getStart());

        PageHelper.startPage(1, continuation.getSize());
        List<Role> roles = roleRepository.selectByExample(example);
        Set<Authorization> authorizations = roles.parallelStream().map(role ->
            new Authorization() {{
                setId(role.getId());
                setRole(role.getRoleExternalId());
                setTenant(role.getTenantId());
            }}
        ).collect(Collectors.toSet());

        return new AuthorizationManagementResult(ManagementStatus.SUCCESS, authorizations, null);
    }

}
