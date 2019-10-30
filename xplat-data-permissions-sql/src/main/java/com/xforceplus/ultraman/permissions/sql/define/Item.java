package com.xforceplus.ultraman.permissions.sql.define;

/**
 * 一个基础项,表示所有的元素.
 * @version 0.1 2019/10/29 15:13
 * @auth dongbin
 * @since 1.8
 */
public interface Item {

    /**
     * 输出 sql 的表示字符串.
     * @return sql 表示字符串.
     */
    String toSqlString();

}
