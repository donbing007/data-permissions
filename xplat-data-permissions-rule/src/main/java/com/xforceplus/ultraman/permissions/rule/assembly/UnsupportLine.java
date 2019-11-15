package com.xforceplus.ultraman.permissions.rule.assembly;

import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.sql.Sql;

import java.io.IOException;

/**
 * @version 0.1 2019/11/14 11:35
 * @auth dongbin
 * @since 1.8
 */
public class UnsupportLine implements Line{
    @Override
    public void start(Context context) throws Throwable {
        throw new UnsupportedOperationException("Unsupported SQL.");
    }

    @Override
    public boolean isSupport(Sql sql) {
        return true;
    }
}
