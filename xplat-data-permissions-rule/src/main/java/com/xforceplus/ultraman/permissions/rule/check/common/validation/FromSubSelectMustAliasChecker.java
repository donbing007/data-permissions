package com.xforceplus.ultraman.permissions.rule.check.common.validation;

import com.sun.jmx.remote.internal.ArrayQueue;
import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitorAdapter;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * 校验From子查询必须有别名.
 * @version 0.1 2019/11/8 11:39
 * @auth dongbin
 * @since 1.8
 */
public class FromSubSelectMustAliasChecker extends AbstractTypeSafeChecker {


    public FromSubSelectMustAliasChecker() {
        super(SqlType.SELECT);
    }

    @Override
    protected void checkTypeSafe(Context context) {

        Queue<Sql> queue = new ArrayDeque<>();
        queue.add(context.sql());

        SelectSqlProcessor processor;
        Sql sql;
        while(!queue.isEmpty()) {
            sql = queue.poll();
            processor = (SelectSqlProcessor) sql.buildProcessor();

            if (!doCheck(processor)) {
                context.refused("The From clause must have an alias.");
                break;
            } else {

                queue.addAll(processor.buildSubSqlAbility().list());

            }

        }
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
