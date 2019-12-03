package com.xforceplus.ultraman.permissions.service.aop;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.Result;
import com.xforceplus.ultraman.permissions.repository.RoleRepository;
import com.xforceplus.ultraman.permissions.repository.entity.Role;
import com.xforceplus.ultraman.permissions.repository.entity.RoleExample;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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

    final Logger logger = LoggerFactory.getLogger(AuthorizationCheckBeforePoint.class);

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

    public Object checkAuthorization(ProceedingJoinPoint joinPoint) throws Throwable {
        if (logger.isDebugEnabled()) {
            Signature signature =  joinPoint.getSignature();
            logger.debug("Intercept {}.{}.", signature.getDeclaringTypeName(), signature.getName());
        }

        Object[] args = joinPoint.getArgs();
        if (args != null) {
            for (Object arg : args) {
                if (Authorization.class.isInstance(arg)) {
                    Optional<Role> roleOptional = findRole((Authorization) arg);
                    if (!roleOptional.isPresent()) {

                        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
                        Method method = sign.getMethod();
                        AuthorizationCheck checkAnnotation = method.getAnnotation(AuthorizationCheck.class);
                        NoAuthorizationPlan plan;
                        if (checkAnnotation == null) {
                            plan = NoAuthorizationPlan.ERROR;
                        } else {
                            plan = checkAnnotation.value();
                        }

                        switch (plan) {
                            case CREATE:
                                return processorCreate((Authorization) arg, joinPoint);
                            default:
                                return processorErr((Authorization) arg, joinPoint);
                        }


                    } else {

                        setAuthorization(((Authorization) arg), roleOptional.get());

                        break;
                    }
                }
            }
        }

        return joinPoint.proceed();
    }

    private Object processorErr(Authorization arg, ProceedingJoinPoint joinPoint) throws Throwable {
        Class returnType = getReturnType(joinPoint);

        if (Result.class.equals(returnType)) {
            return new Result(ManagementStatus.LOSS) {};
        }

        if (Result.class.isAssignableFrom(returnType)) {
            Constructor c = returnType.getConstructor(ManagementStatus.class);
            return c.newInstance(ManagementStatus.LOSS);
        } else {
            throw new Exception("Not a subclass of " + Result.class.toString() + " as expected.");
        }
    }

    private Object processorCreate(Authorization arg, ProceedingJoinPoint joinPoint) throws Throwable  {
        Role role = new Role();
        role.setTenantId(arg.getTenant());
        role.setRoleExternalId(arg.getRole());
        if (roleRepository.insert(role) > 0) {

            setAuthorization(arg, role);
            return joinPoint.proceed();

        } else {
            throw new Exception("The role could not be created.[" + role.toString() +"]");
        }
    }

    private void setAuthorization(Authorization auth, Role role) {
        if (auth.getId() == null) {
            auth.setId(role.getId());
        }
    }

    private Class getReturnType(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature =  joinPoint.getSignature();
        return ((MethodSignature) signature).getReturnType();
    }

    private Optional<Role> findRole(Authorization authorization) {
        RoleExample example = new RoleExample();
        example.createCriteria().andRoleExternalIdEqualTo(authorization.getRole())
            .andTenantIdEqualTo(authorization.getTenant());
        List<Role> roles = roleRepository.selectByExample(example);
        return roles.stream().findFirst();
    }
}
