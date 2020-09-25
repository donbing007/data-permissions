package com.xforceplus.ultraman.permissions.service.aop;

/**
 * 没有权限信息的处理计划.
 *
 * @author dongbin
 * @version 0.1 2019/12/3 11:47
 * @since 1.8
 */
public enum NoAuthorizationPlan {

    UNKNOWN,

    /**
     * 返回错误.
     */
    ERROR,
    /**
     * 创建.
     */
    CREATE,

    /**
     * 初始化未创建任何角色
     */
    PASS;
}
