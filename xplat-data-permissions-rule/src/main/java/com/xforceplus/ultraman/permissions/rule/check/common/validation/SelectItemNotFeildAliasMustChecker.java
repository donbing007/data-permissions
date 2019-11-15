package com.xforceplus.ultraman.permissions.rule.check.common.validation;

import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.*;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.Arithmeitc;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * 如果是非简单 Field,那么必须设置别名.
 *
 * @author dongbin
 * @version 0.1 2019/11/8 17:27
 * @since 1.8
 */
public class SelectItemNotFeildAliasMustChecker extends AbstractTypeSafeChecker {

    public SelectItemNotFeildAliasMustChecker() {
        super(SqlType.SELECT);
    }

    @Override
    protected void checkTypeSafe(Context context) {

        Queue<Sql> queue = new ArrayDeque<>();
        queue.add(context.sql());

        SelectSqlProcessor processor;
        Sql sql;
        while (!queue.isEmpty()) {
            sql = queue.poll();
            processor = (SelectSqlProcessor) sql.buildProcessor();

            if (!doCheck(processor)) {
                context.refused("All return entries must be aliased.");
                break;
            } else {

                queue.addAll(processor.buildSubSqlAbility().list());

            }

        }
    }

    private boolean doCheck(SelectSqlProcessor processor) {
        List<Item> selectItems = processor.buildSelectItemAbility().list();

        AliasFieldItemVisitor visitor = new AliasFieldItemVisitor();

        for (Item item : selectItems) {
            item.visit(visitor);

            if (visitor.isFinding()) {
                return false;
            }
        }

        return true;
    }

    private static class AliasFieldItemVisitor extends ItemVisitorAdapter {

        private boolean finding = false;

        public boolean isFinding() {
            return finding;
        }

        @Override
        public void visit(Func item) {
            if (!item.hasAlias()) {
                finding = true;
            }
        }

        @Override
        public void visit(Parentheses item) {
            if (!item.hasAlias()) {
                finding = true;
            }
        }

        @Override
        public void visit(Arithmeitc item) {
            if (!item.hasAlias()) {
                finding = true;
            }
        }
    }
}
