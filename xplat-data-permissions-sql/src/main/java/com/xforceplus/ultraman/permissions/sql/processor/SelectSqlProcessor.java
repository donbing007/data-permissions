package com.xforceplus.ultraman.permissions.sql.processor;

import com.xforceplus.ultraman.permissions.sql.processor.ability.*;

/**
 * Select 语句的 process 定义.
 *
 * @author dongbin
 * @version 0.1 2019/10/25 17:28
 * @since 1.8
 */
public interface SelectSqlProcessor extends SqlProcessor {

    /**
     * 得到条件操作实例.
     *
     * @return 实例.
     */
    ConditionAbility buildConditionAbility();

    /**
     * 得到查询字段列表操作实例.
     *
     * @return 实例.
     */
    SelectItemAbility buildSelectItemAbility();

    /**
     * 得到当前的嵌套查询和子查询.
     *
     * @return 语句列表.
     */
    SubSqlAbility buildSubSqlAbility();
}
