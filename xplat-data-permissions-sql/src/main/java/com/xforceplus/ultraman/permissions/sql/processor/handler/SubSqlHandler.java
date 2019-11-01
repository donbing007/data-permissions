package com.xforceplus.ultraman.permissions.sql.processor.handler;

import com.xforceplus.ultraman.permissions.sql.Sql;

import java.util.List;

/**
 * 子查询处理.
 * @version 0.1 2019/10/31 13:52
 * @auth dongbin
 * @since 1.8
 */
public interface SubSqlHandler {

    /**
     * 列出当前的所有子句.
     * @return 子句列表.
     */
    List<Sql> list();
}
