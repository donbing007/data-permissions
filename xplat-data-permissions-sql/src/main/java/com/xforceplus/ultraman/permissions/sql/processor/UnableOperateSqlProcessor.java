package com.xforceplus.ultraman.permissions.sql.processor;

/**
 * 表示当前无法进行操作.
 * @version 0.1 2019/10/31 13:54
 * @auth dongbin
 * @since 1.8
 */
public class UnableOperateSqlProcessor implements SqlProcessor {

    private static final UnableOperateSqlProcessor INSTANCE = new UnableOperateSqlProcessor();

    public static UnableOperateSqlProcessor getInstance() {
        return INSTANCE;
    }
}
