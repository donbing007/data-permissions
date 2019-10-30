package com.xforceplus.ultraman.permissions.sql.processor;

import com.xforceplus.ultraman.permissions.sql.processor.handler.ConditionHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.FromHandler;

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
    ConditionHandler buildConditionHandler();

    /**
     * 得到来源表操作实例.
     * @return 实例.
     */
    FromHandler buildFromHandler();

}
