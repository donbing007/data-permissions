package com.xforceplus.ultraman.permissions.sql.processor;

import com.xforceplus.ultraman.permissions.sql.processor.handler.*;

/**
 * 子查询处理.
 * @version 0.1 2019/10/31 19:28
 * @auth dongbin
 * @since 1.8
 */
public interface SubSelectSqlProcessor extends SqlProcessor{

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
     * 得到查询字段列表操作实例.
     * @return 实例.
     */
    SelectItemHandler buildColumnHandler();

    /**
     * 得到当前的嵌套查询和子查询.
     * @return 语句列表.
     */
    SubSqlHandler buildSubSqlHandler();

    /**
     * 得到当前字段实际表名搜索处理器.
     * @return 处理器实例.
     */
    FieldFromHandler buildFieldFromHandler();
}
