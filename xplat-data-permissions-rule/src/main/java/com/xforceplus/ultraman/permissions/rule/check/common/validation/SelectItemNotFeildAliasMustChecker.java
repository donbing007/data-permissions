package com.xforceplus.ultraman.permissions.rule.check.common.validation;

import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.*;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.Arithmeitc;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessor;

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
public class SelectItemNotFeildAliasMustChecker extends AbstractValidationChecker {

    public SelectItemNotFeildAliasMustChecker() {
        super(SqlType.SELECT);
    }

    @Override
    protected boolean doCheck(SqlProcessor processor) {
        List<Item> selectItems = ((SelectSqlProcessor) processor).buildSelectItemAbility().list();

        AliasFieldItemVisitor visitor = new AliasFieldItemVisitor();

        for (Item item : selectItems) {
            item.visit(visitor);

            if (visitor.isFinding()) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected String refusedCause() {
        return "All return entries must be aliased.";
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
