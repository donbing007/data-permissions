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
 * 查询语句不可以使用"*"返回检查器.
 *
 * @author dongbin
 * @version 0.1 2019/11/8 14:15
 * @since 1.8
 */
public class AllFieldCannotUseChecker extends AbstractTypeSafeChecker {


    public AllFieldCannotUseChecker() {
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
                context.refused("Select return values with '*' are not allowed.");
                break;
            } else {

                queue.addAll(processor.buildSubSqlAbility().list());

            }

        }
    }

    private boolean doCheck(SelectSqlProcessor processor) {
        List<Item> selectItems = processor.buildSelectItemAbility().list();

        AllFieldItemVisitor visitor = new AllFieldItemVisitor();

        for (Item item : selectItems) {
            item.visit(visitor);

            if (visitor.isFinding()) {
                return false;
            }
        }

        return true;
    }

    private static class AllFieldItemVisitor extends ItemVisitorAdapter {

        private boolean finding = false;

        public boolean isFinding() {
            return finding;
        }

        @Override
        public void visit(Field item) {
            if (Field.getAllField().getName().equals(item.getName())) {
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
