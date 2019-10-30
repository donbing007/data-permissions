package com.xforceplus.ultraman.permissions.sql.processor.handler;

import com.xforceplus.ultraman.permissions.sql.define.Field;

import java.util.List;

/**
 * insert 字段处理.
 * @version 0.1 2019/10/25 18:20
 * @auth dongbin
 * @since 1.8
 */
public interface InsertItemHandler {

    /**
     *
     * @return 迭代器.
     */
    List<Field> list();
}
