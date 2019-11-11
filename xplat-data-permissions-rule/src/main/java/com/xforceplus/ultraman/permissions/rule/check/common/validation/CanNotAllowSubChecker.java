package com.xforceplus.ultraman.permissions.rule.check.common.validation;

import com.xforceplus.ultraman.permissions.rule.check.Checker;
import com.xforceplus.ultraman.permissions.rule.context.CheckContext;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.processor.DeleteSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitorAdapter;
import com.xforceplus.ultraman.permissions.sql.processor.UpdateSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.SubSqlAbility;

/**
 * 不允许子查询.
 * @version 0.1 2019/11/8 15:28
 * @auth dongbin
 * @since 1.8
 */
public class CanNotAllowSubChecker implements Checker {

    @Override
    public void check(CheckContext context) {

        Sql sql  = context.sql();

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

    private void doCheck(SubSqlAbility ability, CheckContext context) {
        if (ability.list().size() > 0) {
            context.refused("Subqueries are not allowed.");
        }
    }
}
