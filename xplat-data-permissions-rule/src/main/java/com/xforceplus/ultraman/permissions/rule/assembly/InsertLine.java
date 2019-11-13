package com.xforceplus.ultraman.permissions.rule.assembly;

import com.xforceplus.ultraman.permissions.rule.check.Checker;
import com.xforceplus.ultraman.permissions.rule.check.insert.InsertFieldChecker;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;

import java.util.Arrays;
import java.util.List;

/**
 * @version 0.1 2019/11/13 14:00
 * @auth dongbin
 * @since 1.8
 */
public class InsertLine extends AbstractLine {
    public InsertLine(List<Checker> checkers) {
        super(checkers);
    }

    @Override
    protected List<Class<? extends Checker>> selectChecker() {
        return Arrays.asList(
            InsertFieldChecker.class
        );
    }

    @Override
    public boolean isSupport(Sql sql) {
        return sql.type() == SqlType.INSERT;
    }
}
