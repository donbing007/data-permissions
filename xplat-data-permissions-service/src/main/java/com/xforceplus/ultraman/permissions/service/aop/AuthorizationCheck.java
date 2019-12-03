package com.xforceplus.ultraman.permissions.service.aop;

import java.lang.annotation.*;

/**
 * 检查结果标记.
 * @author dongbin
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AuthorizationCheck {

    NoAuthorizationPlan value() default NoAuthorizationPlan.ERROR;


}
