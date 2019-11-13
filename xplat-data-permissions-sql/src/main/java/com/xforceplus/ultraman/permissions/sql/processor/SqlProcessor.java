package com.xforceplus.ultraman.permissions.sql.processor;

import com.xforceplus.ultraman.permissions.sql.processor.ability.FromAbility;

/**
 * SQL 操作定义接口.
 *
 * @version 0.1 2019/10/25 16:03
 * @auth dongbin
 * @since 1.8
 */
public interface SqlProcessor {

    /**
     * 得到来源表操作实例.
     * @return 实例.
     */
    FromAbility buildFromAbility();
}
