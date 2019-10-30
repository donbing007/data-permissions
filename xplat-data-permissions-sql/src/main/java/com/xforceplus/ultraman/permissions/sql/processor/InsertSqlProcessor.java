package com.xforceplus.ultraman.permissions.sql.processor;

import com.xforceplus.ultraman.permissions.sql.processor.handler.FromHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.InsertItemHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.InsertValueHandler;

/**
 * 插入语句操作定义.
 * @version 0.1 2019/10/25 17:33
 * @auth dongbin
 * @since 1.8
 */
public interface InsertSqlProcessor extends SqlProcessor {

    /**
     * 得到来源表操作实例.
     * @return 实例.
     */
    FromHandler buildFromHandler();

    /**
     * 得到当胶插入语句的值的操作实现.
     * @return 实例.
     */
    InsertValueHandler buildInsertValueHandler();

    /**
     * 得到操作插入字段的操作实例.
     * @return 实例.
     */
    InsertItemHandler buildInsertItemHandler();
}
