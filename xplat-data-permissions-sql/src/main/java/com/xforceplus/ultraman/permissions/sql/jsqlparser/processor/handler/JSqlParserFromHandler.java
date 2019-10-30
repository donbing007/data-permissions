package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler;

import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.processor.handler.FromHandler;
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
public class JSqlParserFromHandler extends AbstractJSqlParserHandler implements FromHandler {

    public JSqlParserFromHandler(Statement statement) {
        super(statement);

    }

    @Override
    public List<From> list() {

        List<From> items = new ArrayList<>();
        getStatement().accept(new StatementVisitorAdapter() {
            @Override
            public void visit(Select select) {
                SelectBody body = select.getSelectBody();
                if (body != null) {
                    body.accept(new SelectVisitorAdapter() {
                        @Override
                        public void visit(PlainSelect plainSelect) {
                            FromItem fromItem = plainSelect.getFromItem();
                            if (fromItem != null) {
                                fromItem.accept(new FromItemVisitorAdapter() {
                                    @Override
                                    public void visit(Table table) {
                                        From from = new From(table.getName(),
                                            table.getAlias() != null ? table.getAlias().getName() : null);
                                        items.add(from);
                                    }

                                    @Override
                                    public void visit(SubSelect subSelect) {
                                        From from = new From("",
                                            subSelect.getAlias() != null ? subSelect.getAlias().getName() : null, true);
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
                                            From from = new From(table.getName(),
                                                table.getAlias() != null ? table.getAlias().getName() : null);
                                            items.add(from);
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void visit(Delete delete) {
                Table table = delete.getTable();
                From from = new From(table.getName(),table.getAlias().getName());
                items.add(from);
            }

            @Override
            public void visit(Update update) {
                Table table = update.getTable();
                From from = new From(table.getName(),table.getAlias().getName());
                items.add(from);
            }
        });

        return items;
    }
}
