package com.xforceplus.ultraman.permissions.rule.check.common.validation;

import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.*;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.Arithmeitc;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitorAdapter;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * select 的 selectItem 必须设置来源表名或者表别名.
 * select c1 from t1 错误.
 * select t1.c1 from t1 正确
 * select t.c1 from t1 t 正确.
 * @version 0.1 2019/11/8 14:40
 * @auth dongbin
 * @since 1.8
 */
public class SelectItemRefMustChecker extends AbstractTypeSafeChecker {

    public SelectItemRefMustChecker() {
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
                context.refused("All return entries must be aliased.");
                break;
            } else {

                queue.addAll(processor.buildSubSqlAbility().list());

            }

        }

    }

    private boolean doCheck(SelectSqlProcessor processor) {
        List<Item> selectItems = processor.buildSelectItemAbility().list();

        RefFieldItemVisitor visitor = new RefFieldItemVisitor();

        for (Item item : selectItems) {
            item.visit(visitor);

            if (visitor.isFinding()) {
                return false;
            }
        }

        return true;
    }

    private static class RefFieldItemVisitor extends ItemVisitorAdapter {

        private boolean finding = false;

        public boolean isFinding() {
            return finding;
        }

        @Override
        public void visit(Field item) {
            if (item.getRef() == null) {
                finding = true;
            }
        }

        @Override
        public void visit(Func item) {
            if (!finding) {
                for (Item p : item.getParameters()) {
                    p.visit(this);
                }
            }
        }

        @Override
        public void visit(Parentheses item) {
            if (!finding) {
                item.getItem().visit(this);
            }
        }

        @Override
        public void visit(Arithmeitc item) {
            if (!finding) {
                item.getLeft().visit(this);
                item.getRight().visit(this);
            }
        }
    }
}
