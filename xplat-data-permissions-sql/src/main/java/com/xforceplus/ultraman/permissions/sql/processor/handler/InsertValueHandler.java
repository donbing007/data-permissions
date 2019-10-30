package com.xforceplus.ultraman.permissions.sql.processor.handler;

import com.xforceplus.ultraman.permissions.sql.define.Item;

import java.util.List;

/**
 * insert values 子句.
 * @version 0.1 2019/10/30 17:19
 * @auth dongbin
 * @since 1.8
 */
public interface InsertValueHandler {

    /**
     * 得到指定序号的值列表.
     * @return 值列表.
     */
    List<Item> list(int index);

    /**
     * 返回有多少个值列表.
     * @return 列表数量.
     */
    int size();
}
