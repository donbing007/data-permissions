package com.xforceplus.ultraman.permissions.rule.assembly;

import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;

/**
 * 流水线
 * @version 0.1 2019/11/13 11:48
 * @auth dongbin
 * @since 1.8
 */
public interface Line {

    /**
     * 开始工作.
     * @param context 上下文.
     */
    void start(Context context) throws Throwable;

    /**
     * 是否支持指定 sql.
     * @param sql 目标 sql.
     * @return true 支持,false 不支持.
     */
    boolean isSupport(Sql sql);
}
