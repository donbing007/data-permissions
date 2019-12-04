package com.xforceplus.ultraman.permissions.rule.assembly;

import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.check.Checker;
import com.xforceplus.ultraman.permissions.rule.check.common.ConditionsChecker;
import com.xforceplus.ultraman.permissions.rule.check.common.ConditionsFieldChecker;
import com.xforceplus.ultraman.permissions.rule.check.common.validation.AllFieldCannotUseChecker;
import com.xforceplus.ultraman.permissions.rule.check.common.validation.FromSubSelectMustAliasChecker;
import com.xforceplus.ultraman.permissions.rule.check.common.validation.SelectItemNotFeildAliasMustChecker;
import com.xforceplus.ultraman.permissions.rule.check.common.validation.SelectItemRefMustChecker;
import com.xforceplus.ultraman.permissions.rule.check.select.SelectFieldChecker;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;

import java.util.Arrays;
import java.util.List;

/**
 * @version 0.1 2019/11/13 12:20
 * @author dongbin
 * @since 1.8
 */
public class SelectLine extends AbstractLine {

    public SelectLine(List<Checker> checkers) {
        super(checkers);
    }

    @Override
    protected List<Class<? extends Checker>> selectChecker() {
        return Arrays.asList(
            AllFieldCannotUseChecker.class,
            FromSubSelectMustAliasChecker.class,
            SelectItemNotFeildAliasMustChecker.class,
            SelectItemRefMustChecker.class,
            SelectFieldChecker.class,
            ConditionsFieldChecker.class,
            ConditionsChecker.class
        );
    }

    @Override
    public boolean isSupport(Sql sql) {
        return sql.type() == SqlType.SELECT;
    }
}
