package com.xforceplus.ultraman.permissions.sql.processor;

import com.xforceplus.ultraman.permissions.sql.processor.handler.ConditionHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.FieldFromHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.FromHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.UpdateSetHandler;

/**
 * update 语句的 processor 定义.
 * @version 0.1 2019/10/25 17:30
 * @auth dongbin
 * @since 1.8
 */
public interface UpdateSqlProcessor extends SqlProcessor {
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

    /**
     * 得到更新语句 set 操作实例.
     * @return 实例.
     */
    UpdateSetHandler buildUpdateSetHandler();

    /**
     * 字段来源搜索器.
     * @return 字段来源.
     */
    FieldFromHandler buildFieldFromHandler();
}
