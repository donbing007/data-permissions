package com.xforceplus.ultraman.permissions.sql.processor;

import com.xforceplus.ultraman.permissions.sql.processor.ability.*;

/**
 * update 语句的 processor 定义.
 *
 * @author dongbin
 * @version 0.1 2019/10/25 17:30
 * @since 1.8
 */
public interface UpdateSqlProcessor extends SqlProcessor {
    /**
     * 得到条件操作实例.
     *
     * @return 实例.
     */
    ConditionAbility buildConditionAbility();

    /**
     * 得到更新语句 set 操作实例.
     *
     * @return 实例.
     */
    UpdateSetAbility buildUpdateSetAbility();

    /**
     * 得到当前的嵌套查询和子查询.
     *
     * @return 语句列表.
     */
    SubSqlAbility buildSubSqlAbility();
}
