package com.xforceplus.ultraman.permissions.rule.check.common.validation;

import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessor;
import com.xforceplus.ultraman.permissions.sql.utils.SubSqlIterator;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * 校验From子查询必须有别名.
 *
 * @author dongbin
 * @version 0.1 2019/11/8 11:39
 * @since 1.8
 */
public class FromSubSelectMustAliasChecker extends AbstractValidationChecker {


    public FromSubSelectMustAliasChecker() {
        super(SqlType.SELECT);
    }

    @Override
    protected boolean doCheck(SqlProcessor processor) {
        List<From> froms = processor.buildFromAbility().list();
        for (From from : froms) {
            if (from.isSub() && !from.hasAlias()) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected String refusedCause() {
        return "The From clause must have an alias.";
    }
}
