package com.xforceplus.ultraman.permissions.sql;


import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessor;

import java.util.List;

/**
 * SQL解析结果表示.
 * @version 0.1 2019/10/25 16:03
 * @auth dongbin
 * @since 1.8
 */
public interface Sql {

    /**
     * 得到当前SQL 的字符串表示.
     * @return 完整字符串.
     */
    String toString();

    /**
     * 得到当前 SQL 的操作实例.
     * 通过此实例可以对 SQL 进行修改.
     *
     * @return 操作实例.
     */
    SqlProcessor buildProcessor();

    /**
     * 得到当前 SQL 的类型.
     * @return 类型实例.
     */
    SqlType type();

    /**
     * 返回子查询语句,如果有的话.没有则返回 null.
     * @return 子查询.
     */
    List<Sql> subSql();
}
