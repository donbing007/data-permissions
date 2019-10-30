package com.xforceplus.ultraman.permissions.sql.processor.handler;

import com.xforceplus.ultraman.permissions.sql.define.From;

import java.util.List;

/**
 * From 子句.
 * @version 0.1 2019/10/25 17:50
 * @auth dongbin
 * @since 1.8
 */
public interface FromHandler {

    /**
     * 迭代当前 SQL 的来源表.
     * @return 迭代器.
     */
    List<From> list();

}
