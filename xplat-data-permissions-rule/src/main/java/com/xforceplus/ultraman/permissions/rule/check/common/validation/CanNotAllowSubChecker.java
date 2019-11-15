package com.xforceplus.ultraman.permissions.rule.check.common.validation;

import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.processor.DeleteSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitorAdapter;
import com.xforceplus.ultraman.permissions.sql.processor.UpdateSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.SubSqlAbility;

/**
 * 不允许子查询.
 *
 * @author dongbin
 * @version 0.1 2019/11/8 15:28
 * @since 1.8
 */
public class CanNotAllowSubChecker extends AbstractTypeSafeChecker {

    public CanNotAllowSubChecker() {
        super(new SqlType[]{
            SqlType.UPDATE,
            SqlType.DELETE
        });
    }

    @Override
    protected void checkTypeSafe(Context context) {
        Sql sql = context.sql();

        sql.visit(new SqlProcessorVisitorAdapter() {
            @Override
            public void visit(DeleteSqlProcessor processor) {
                doCheck(processor.buildSubSqlAbility(), context);
            }

            @Override
            public void visit(UpdateSqlProcessor processor) {
                doCheck(processor.buildSubSqlAbility(), context);
            }

        });
    }

    private void doCheck(SubSqlAbility ability, Context context) {
        if (ability.list().size() > 0) {
            context.refused("Subqueries are not allowed.");
        }
    }
}
