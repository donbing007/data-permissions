package com.xforceplus.ultraman.permissions.rule.check.common;

import com.xforceplus.ultraman.permissions.rule.check.Checker;
import com.xforceplus.ultraman.permissions.rule.context.CheckContext;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.Condition;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.processor.DeleteSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitorAdapter;
import com.xforceplus.ultraman.permissions.sql.processor.UpdateSqlProcessor;

import java.util.AbstractMap;
import java.util.List;

/**
 * 语句的条件检查,会修改原始SQL.
 * @version 0.1 2019/11/8 17:12
 * @auth dongbin
 * @since 1.8
 */
public class ConditionsChecker implements Checker {

    @Override
    public void check(CheckContext context) {
        Sql sql = context.sql();

        sql.visit(new SqlProcessorVisitorAdapter() {
            @Override
            public void visit(SelectSqlProcessor processor) {
                doCheckSelect(processor, context);
            }

            @Override
            public void visit(DeleteSqlProcessor processor) {
                super.visit(processor);
            }

            @Override
            public void visit(UpdateSqlProcessor processor) {
                super.visit(processor);
            }
        });
    }

    private void doCheckSelect(SelectSqlProcessor processor, CheckContext context) {

    }


}
