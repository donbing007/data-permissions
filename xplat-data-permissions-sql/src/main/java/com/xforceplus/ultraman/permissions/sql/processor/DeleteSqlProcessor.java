package com.xforceplus.ultraman.permissions.sql.processor;

import com.xforceplus.ultraman.permissions.sql.processor.ability.ConditionAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FromAbility;

/**
 * 删除语句操作定义.
 * @version 0.1 2019/10/25 17:32
 * @auth dongbin
 * @since 1.8
 */
public interface DeleteSqlProcessor extends SqlProcessor {

    /**
     * 得到条件操作实例.
     *
     * @return 实例.
     */
    ConditionAbility buildConditionAbility();

    /**
     * 得到来源表操作实例.
     * @return 实例.
     */
    FromAbility buildFromAbility();

    /**
     * 得到当前字段实际表名搜索处理器.
     * @return 处理器实例.
     */
    FieldFromAbility buildFieldFromAbility();

}
