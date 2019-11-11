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
 * 如果是非简单 Field,那么必须设置别名.
 * @version 0.1 2019/11/8 17:27
 * @auth dongbin
 * @since 1.8
 */
public class SelectItemNotFeildAliasMustChecker implements Checker {

    @Override
    public void check(CheckContext context) {
        Sql sql = context.sql();

        sql.visit(new SqlProcessorVisitorAdapter() {
            @Override
            public void visit(SelectSqlProcessor processor) {
                if (!doCheck(processor)) {
                    context.refused("All return entries must be aliased.");
                    return;
                }

                List<Sql> subSqls = processor.buildSubSqlAbility().list();
                for (Sql subSql : subSqls) {
                    if (!doCheck((SelectSqlProcessor) subSql.buildProcessor())) {
                        context.refused("All return entries must be aliased.");
                        return;
                    }
                }
            }
        });

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
