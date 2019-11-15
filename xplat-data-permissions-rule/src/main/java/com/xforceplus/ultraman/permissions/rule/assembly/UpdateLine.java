package com.xforceplus.ultraman.permissions.rule.assembly;

import com.xforceplus.ultraman.permissions.rule.check.Checker;
import com.xforceplus.ultraman.permissions.rule.check.common.ConditionsChecker;
import com.xforceplus.ultraman.permissions.rule.check.common.validation.CanNotAllowSubChecker;
import com.xforceplus.ultraman.permissions.rule.check.update.UpdateSetFieldChecker;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;

import java.util.Arrays;
import java.util.List;

/**
 * @version 0.1 2019/11/13 13:57
 * @author dongbin
 * @since 1.8
 */
public class UpdateLine extends AbstractLine {
    public UpdateLine(List<Checker> checkers) {
        super(checkers);
    }

    @Override
    protected List<Class<? extends Checker>> selectChecker() {
        return Arrays.asList(
            CanNotAllowSubChecker.class,
            UpdateSetFieldChecker.class,
            ConditionsChecker.class
        );
    }

    @Override
    public boolean isSupport(Sql sql) {
        return sql.type() == SqlType.UPDATE;
    }
}
