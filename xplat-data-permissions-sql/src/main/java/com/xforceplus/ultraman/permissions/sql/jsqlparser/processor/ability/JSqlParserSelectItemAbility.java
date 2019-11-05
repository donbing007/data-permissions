package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.Func;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.values.ArithmeticValue;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ConversionHelper;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ValueHelper;
import com.xforceplus.ultraman.permissions.sql.processor.ability.SelectItemAbility;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.TimeKeyExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JSqlParser 字段操作.
 *
 * @version 0.1 2019/10/28 17:23
 * @auth dongbin
 * @since 1.8
 */
public class JSqlParserSelectItemAbility extends AbstractJSqlParserHandler implements SelectItemAbility {

    public JSqlParserSelectItemAbility(PlainSelect plainSelect) {
        super(plainSelect);
    }

    public JSqlParserSelectItemAbility(Statement statement) {
        super(statement, Select.class);
    }


    @Override
    public void remove(Func func) {
        doRemove(func);
    }

    @Override
    public void remove(Field field) {
        doRemove(field);
    }


    private void doRemove(Item item) {
        String sqlString = item.toSqlString();
        if (isSubSelect()) {
            getSubSelect().accept(new RemoveSelectVisitorImpl(sqlString));
        } else {
            SelectBody selectBody = getSelect().getSelectBody();
            selectBody.accept(new RemoveSelectVisitorImpl(sqlString));
        }
    }


    @Override
    public List<Item> list() {
        List<Item> selectFields = new ArrayList<>();

        if (isSubSelect()) {

            getSubSelect().accept(new ListSelectVisitorImpl(selectFields));

        } else {
            SelectBody selectBody = getSelect().getSelectBody();
            selectBody.accept(new ListSelectVisitorImpl(selectFields));
        }

        return selectFields;
    }

    private static class ListSelectVisitorImpl extends SelectVisitorAdapter {

        private List<Item> selectFields;

        public ListSelectVisitorImpl(List<Item> selectFields) {
            this.selectFields = selectFields;
        }

        @Override
        public void visit(PlainSelect plainSelect) {

            List<SelectItem> items = plainSelect.getSelectItems();
            if (items == null) {
                return;
            }

            for (SelectItem item : items) {
                item.accept(new SelectItemVisitorAdapter() {
                    @Override
                    public void visit(AllColumns columns) {
                        selectFields.add(Field.getAllField());
                    }

                    @Override
                    public void visit(SelectExpressionItem item) {
                        final Alias alias;
                        if (item.getAlias() != null) {

                            alias = item.getAlias();

                        } else {

                            alias = null;

                        }

                        if (ValueHelper.isValueExpr(item.getExpression())) {

                            selectFields.add(ConversionHelper.convertValue(item.getExpression()));

                        } else if (ValueHelper.isArithmeticExpr(item.getExpression())) {

                            selectFields.add(new ArithmeticValue(item.getExpression().toString()));

                        } else {

                            item.getExpression().accept(new ExpressionVisitorAdapter() {
                                @Override
                                public void visit(Column column) {

                                    selectFields.add(ConversionHelper.convert(column, alias));

                                }

                                @Override
                                public void visit(Function function) {

                                    selectFields.add(ConversionHelper.convert(function, alias));

                                }

                                @Override
                                public void visit(TimeKeyExpression timeKeyExpression) {

                                    selectFields.add(ConversionHelper.convert(timeKeyExpression, alias));
                                }
                            });
                        }
                    }
                });

            }

        }
    }

    private static class RemoveSelectVisitorImpl extends SelectVisitorAdapter {

        private String sqlString;

        public RemoveSelectVisitorImpl(String sqlString) {
            this.sqlString = sqlString;
        }

        @Override
        public void visit(PlainSelect plainSelect) {
            List<SelectItem> selectItems = plainSelect.getSelectItems();
            if (selectItems != null) {
                List<SelectItem> newSelectItems = selectItems.stream()
                    .filter(s -> !s.toString().equals(sqlString)).collect(Collectors.toList());
                plainSelect.setSelectItems(newSelectItems);
            }
        }

        @Override
        public void visit(SetOperationList setOpList) {
            visit((PlainSelect) setOpList.getSelects().get(0));
        }
    }

}
