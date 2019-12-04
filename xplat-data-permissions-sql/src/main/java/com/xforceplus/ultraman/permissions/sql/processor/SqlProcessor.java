package com.xforceplus.ultraman.permissions.sql.processor;

import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FromAbility;

/**
 * SQL 操作定义接口.
 *
 * @author dongbin
 * @version 0.1 2019/10/25 16:03
 * @since 1.8
 */
public interface SqlProcessor {

    /**
     * 得到来源表操作实例.
     *
     * @return 实例.
     */
    FromAbility buildFromAbility();

    /**
     * 字段来源搜索器.
     *
     * @return 字段来源.
     */
    FieldFromAbility buildFieldFromAbility();
}
