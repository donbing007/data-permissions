package com.xforceplus.ultraman.permissions.rule.check.common.validation;

import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessor;
import com.xforceplus.ultraman.permissions.sql.utils.SubSqlIterator;

import java.util.Iterator;

/**
 * 校验父类.子类只需要定义检查方法即可.
 *
 * @version 0.1 2019/11/19 19:45
 * @author dongbin
 * @since 1.8
 */
public abstract class AbstractValidationChecker extends AbstractTypeSafeChecker {

    public AbstractValidationChecker(SqlType type) {
        super(type);
    }

    public AbstractValidationChecker(SqlType[] types) {
        super(types);
    }

    @Override
    protected void checkTypeSafe(Context context) {
        SelectSqlProcessor processor = (SelectSqlProcessor) context.sql().buildProcessor();
        if (!doCheck(processor)) {
            context.refused(refusedCause());
            return;
        }

        Iterator<Sql> subSqlIterator = new SubSqlIterator(processor.buildSubSqlAbility());
        Sql sql;
        while (subSqlIterator.hasNext()) {
            sql = subSqlIterator.next();

            if (!doCheck((SelectSqlProcessor) sql.buildProcessor())) {
                context.refused(refusedCause());
                break;
            }
        }
    }

    protected abstract boolean doCheck(SqlProcessor processor);

    protected abstract String refusedCause();
}
