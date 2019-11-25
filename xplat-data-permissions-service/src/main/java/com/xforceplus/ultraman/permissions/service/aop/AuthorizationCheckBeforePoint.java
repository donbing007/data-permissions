package com.xforceplus.ultraman.permissions.service.aop;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.FieldRuleManagementResult;
import com.xforceplus.ultraman.permissions.repository.RoleRepository;
import com.xforceplus.ultraman.permissions.repository.entity.Role;
import com.xforceplus.ultraman.permissions.repository.entity.RoleExample;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 定义了对方法中参数 Authorization 实例中记录的授权信息进行有效性检测的拦截实现.
 *
 * @version 0.1 2019/11/22 14:57
 * @author dongbin
 * @since 1.8
 */
@Aspect
@Component
public class AuthorizationCheckBeforePoint {

    @Resource
    private RoleRepository roleRepository;

    @Around("execution(* com.xforceplus.ultraman.permissions.service.impl.RuleFieldRuleManagementServiceImpl.*(..))")
    public Object checkField(ProceedingJoinPoint joinPoint) throws Throwable {
        return checkAuthorization(joinPoint);
    }

    @Around("execution(* com.xforceplus.ultraman.permissions.service.impl.RuleDataRuleManagementServiceImpl.*(..))")
    public Object checkData(ProceedingJoinPoint joinPoint) throws Throwable {
        return checkAuthorization(joinPoint);
    }

    private Object checkAuthorization(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (Authorization.class.isInstance(arg)) {
                Optional<Role> roleOptional = findRole((Authorization) arg);
                if (!roleOptional.isPresent()) {

                    Class returnType = getReturnType(joinPoint);
                    if (FieldRuleManagementResult.class.equals(returnType)) {

                        return new FieldRuleManagementResult(ManagementStatus.LOSS, "Nonexistent authorization information.");

                    } else if (List.class.equals(returnType)) {

                        return Collections.emptyList();
                    }

                } else {

                    Authorization auth = ((Authorization) arg);
                    if (auth.getId() == null) {
                        auth.setId(roleOptional.get().getId());
                    }

                    break;
                }
            }
        }

        return joinPoint.proceed();
    }

    private Class getReturnType(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        List<Class> classList = Arrays.stream(args).map(arg -> arg.getClass()).collect(Collectors.toList());
        Object target = joinPoint.getTarget();
        Method method = target.getClass().getMethod(joinPoint.getSignature().getName(), classList.toArray(new Class[0]));
        return method.getReturnType();
    }

    private Optional<Role> findRole(Authorization authorization) {
        RoleExample example = new RoleExample();
        example.createCriteria().andRoleExternalIdEqualTo(authorization.getRole())
            .andTenantIdEqualTo(authorization.getTenant());
        List<Role> roles = roleRepository.selectByExample(example);
        return roles.stream().findFirst();
    }
}
