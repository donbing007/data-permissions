package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ConversionHelper;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FromAbility;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * JSqlParser from 子句操作.
 *
 * @author dongbin
 * @version 0.1 2019/10/28 15:43
 * @since 1.8
 */
public class JSqlParserFromAbility extends AbstractJSqlParserHandler implements FromAbility {

    public JSqlParserFromAbility(PlainSelect plainSelect) {
        super(plainSelect);
    }

    public JSqlParserFromAbility(Statement statement) {
        super(statement);
    }

    @Override
    public List<From> list() throws ProcessorException {

        List<From> items = new ArrayList<>();

        if (isSubSelect()) {
            getSubSelect().accept(new SelectVisitImpl(items));
        } else {

            getStatement().accept(new StatementVisitorAdapter() {
                @Override
                public void visit(Select select) {
                    SelectBody body = select.getSelectBody();
                    if (body != null) {
                        body.accept(new SelectVisitImpl(items));
                    }
                }

                @Override
                public void visit(Delete delete) {
                    Table table = delete.getTable();
                    items.add(ConversionHelper.convert(table));
                }

                @Override
                public void visit(Update update) {
                    Table table = update.getTable();
                    items.add(ConversionHelper.convert(table));
                }

                @Override
                public void visit(Insert insert) {
                    Table table = insert.getTable();
                    items.add(ConversionHelper.convert(table));
                }

            });
        }

        return items;
    }

    /**
     * union 和 union all 不处理,由专门的子句处理器处理.
     * 子查询,会将子查询整个语句的字符串当做表名.
     */
    private static class SelectVisitImpl extends SelectVisitorAdapter {

        private List<From> items;
        private FromItemVisitor visitor = new FromItemVisitorAdapter() {
            @Override
            public void visit(Table table) {
                From from = new From(table.getName(), ConversionHelper.convert(table.getAlias()));
                items.add(from);
            }

            @Override
            public void visit(SubSelect subSelect) {
                From from = new From(subSelect.getSelectBody().toString(),
                    subSelect.getAlias() != null ? ConversionHelper.convert(subSelect.getAlias()) : null, true);
                items.add(from);
            }
        };


        public SelectVisitImpl(List<From> items) {
            this.items = items;
        }

        @Override
        public void visit(PlainSelect plainSelect) {
            FromItem fromItem = plainSelect.getFromItem();

            if (fromItem != null) {
                fromItem.accept(visitor);
            }

            List<Join> joins = plainSelect.getJoins();
            if (joins != null) {
                for (Join join : joins) {
                    join.getRightItem().accept(visitor);
                }
            }
        }
    }


}
