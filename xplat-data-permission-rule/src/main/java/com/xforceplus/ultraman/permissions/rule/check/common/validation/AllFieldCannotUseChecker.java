package com.xforceplus.ultraman.permissions.rule.check.common.validation;

import com.xforceplus.ultraman.permissions.rule.check.Checker;
import com.xforceplus.ultraman.permissions.rule.context.CheckContext;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.*;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.Arithmeitc;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitorAdapter;

import java.util.List;

/**
 * 查询语句不可以使用"*"返回检查器.
 *
 * @version 0.1 2019/11/8 14:15
 * @auth dongbin
 * @since 1.8
 */
public class AllFieldCannotUseChecker implements Checker {


    @Override
    public void check(CheckContext context) {
        Sql sql = context.sql();

        sql.visit(new SqlProcessorVisitorAdapter() {
            @Override
            public void visit(SelectSqlProcessor processor) {
                if (!doCheck(processor)) {
                    context.refused("Select return values with '*' are not allowed.");
                    return;
                }

                List<Sql> subSqls = processor.buildSubSqlAbility().list();
                for (Sql subSql : subSqls) {
                    if (!doCheck((SelectSqlProcessor) subSql.buildProcessor())) {
                        context.refused("Select return values with '*' are not allowed.");
                        return;
                    }
                }
            }
        });

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
