package com.xforceplus.ultraman.permissions.rule.check.common.validation;

import com.xforceplus.ultraman.permissions.rule.check.Checker;
import com.xforceplus.ultraman.permissions.rule.context.CheckContext;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitorAdapter;

import java.util.List;

/**
 * 校验From子查询必须有别名.
 * @version 0.1 2019/11/8 11:39
 * @auth dongbin
 * @since 1.8
 */
public class FromSubSelectMustAliasChecker implements Checker {

    @Override
    public void check(CheckContext context) {
        Sql sql = context.sql();

        sql.visit(new SqlProcessorVisitorAdapter() {
            @Override
            public void visit(SelectSqlProcessor processor) {

                if (!doCheck(processor)) {
                    context.refused("The From clause must have an alias.");
                }

                // 处理子查询.
                if (!context.isRefused()) {
                    List<Sql> subSqls = processor.buildSubSqlAbility().list();
                    for (Sql subSql : subSqls) {
                        if (!doCheck((SelectSqlProcessor) subSql.buildProcessor())) {
                            context.refused("The From clause must have an alias.");
                            break;
                        }
                    }

                }
            }
        });
    }

    private boolean doCheck(SelectSqlProcessor processor) {
        List<From> froms = processor.buildFromAbility().list();
        for (From from : froms) {
            if (from.isSub() && !from.hasAlias()) {
                return false;
            }
        }

        return true;
    }
}
