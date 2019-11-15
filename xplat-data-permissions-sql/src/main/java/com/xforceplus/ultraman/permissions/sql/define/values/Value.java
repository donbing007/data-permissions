package com.xforceplus.ultraman.permissions.sql.define.values;

import com.xforceplus.ultraman.permissions.sql.define.Item;

/**
 * 表示一个值.
 *
 * @param <T> 元素的实际类型.
 * @author dongbin
 * @version 0.1 2019/10/29 15:17
 * @since 1.8
 */
public interface Value<T> extends Item {

    /**
     * 获取实际值.
     *
     * @return 实际值.
     */
    T getValue();

    /**
     * 如果转换成 sql 表示是否需要单引号.
     *
     * @return
     */
    boolean needQuotes();
}
