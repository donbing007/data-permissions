package com.xforceplus.ultraman.permissions.rule.check;

import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;

/**
 * sql type safe abstract checker.
 * @version 0.1 2019/11/12 19:15
 * @auth dongbin
 * @since 1.8
 */
public abstract class AbstractTypeSafeChecker implements Checker{

    private SqlType[] supportTypes;

    public AbstractTypeSafeChecker(SqlType type) {
        supportTypes = new SqlType[] { type };
    }

    public AbstractTypeSafeChecker(SqlType[] types) {
        this.supportTypes = types;
    }

    @Override
    public void check(Context context) {
        Sql sql = context.sql();
        for (SqlType type : supportTypes) {
            if (type == sql.type()) {
                checkTypeSafe(context);
                return;
            }
        }

        throw new IllegalStateException("Cannot handle SQL statements of type " + sql.type().name() + ".");
    }

    protected abstract void checkTypeSafe(Context context);
}
