package com.xforceplus.ultraman.permissions.sql.define.values;

import com.xforceplus.ultraman.permissions.sql.define.Item;

/**
 * 表示一个值.
 * @version 0.1 2019/10/29 15:17
 * @auth dongbin
 * @since 1.8
 */
public interface Value<T> extends Item {

    /**
     * 获取实际值.
     * @return 实际值.
     */
    T getValue();

    /**
     * 如果转换成 sql 表示是否需要单引号.
     * @return
     */
    boolean needQuotes();
}
