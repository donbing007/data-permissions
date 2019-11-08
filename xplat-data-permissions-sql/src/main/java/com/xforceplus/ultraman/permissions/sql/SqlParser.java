package com.xforceplus.ultraman.permissions.sql;

import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;

/**
 * sql 解析接口.
 * @version 0.1 2019/10/25 16:02
 * @auth dongbin
 * @since 1.8
 */
public interface SqlParser {

    /**
     * 解析 SQL 得到 SQL 实例.
     * @param sql sql 语句.
     * @return 解析结果.
     * @throws ProcessorException 表示 sql 解析失败.
     */
    Sql parser(String sql) throws ProcessorException;

    /**
     * 判断指定的 sql 是否可以解析.
     * @param sql 解析目标字串.
     * @return true 可以支持,false 不支持.
     */
    boolean isSupport(String sql);

}
