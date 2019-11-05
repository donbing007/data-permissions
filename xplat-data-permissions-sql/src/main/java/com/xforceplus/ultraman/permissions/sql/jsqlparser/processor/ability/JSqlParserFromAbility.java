package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ConversionHelper;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FromAbility;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * JSqlParser from 子句操作.
 * @version 0.1 2019/10/28 15:43
 * @auth dongbin
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
    public List<From> list() {

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
                    From from = new From(table.getName(), ConversionHelper.convert(table.getAlias()));
                    items.add(from);
                }

                @Override
                public void visit(Update update) {
                    Table table = update.getTable();
                    From from = new From(table.getName(), ConversionHelper.convert(table.getAlias()));
                    items.add(from);
                }
            });
        }

        return items;
    }

    // union 和 union all 将不处理.
    private static class SelectVisitImpl extends SelectVisitorAdapter {

        private List<From> items;

        public SelectVisitImpl(List<From> items) {
            this.items = items;
        }

        @Override
        public void visit(PlainSelect plainSelect) {
            FromItem fromItem = plainSelect.getFromItem();
            if (fromItem != null) {
                fromItem.accept(new FromItemVisitorAdapter() {
                    @Override
                    public void visit(Table table) {
                        From from = new From(table.getName(), ConversionHelper.convert(table.getAlias()));
                        items.add(from);
                    }

                    @Override
                    public void visit(SubSelect subSelect) {
                        From from = new From("",
                            subSelect.getAlias() != null ? ConversionHelper.convert(subSelect.getAlias()) : null, true);
                        items.add(from);
                    }
                });
            }

            List<Join> joins = plainSelect.getJoins();
            if (joins != null) {
                for (Join join : joins) {
                    join.getRightItem().accept(new FromItemVisitorAdapter() {
                        @Override
                        public void visit(Table table) {
                            From from = new From(table.getName(),ConversionHelper.convert(table.getAlias()));
                            items.add(from);
                        }
                    });
                }
            }
        }
    }
}
