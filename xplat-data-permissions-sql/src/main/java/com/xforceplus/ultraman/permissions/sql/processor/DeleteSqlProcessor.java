package com.xforceplus.ultraman.permissions.sql.processor;

import com.xforceplus.ultraman.permissions.sql.processor.ability.ConditionAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.SubSqlAbility;

/**
 * 删除语句操作定义.
 *
 * @author dongbin
 * @version 0.1 2019/10/25 17:32
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
     * 得到当前字段实际表名搜索处理器.
     *
     * @return 处理器实例.
     */
    FieldFromAbility buildFieldFromAbility();

    /**
     * 得到当前的嵌套查询和子查询.
     *
     * @return 语句列表.
     */
    SubSqlAbility buildSubSqlAbility();

}
