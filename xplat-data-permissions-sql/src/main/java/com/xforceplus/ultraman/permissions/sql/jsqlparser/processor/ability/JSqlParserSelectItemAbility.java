package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.Func;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ConversionHelper;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;
import com.xforceplus.ultraman.permissions.sql.processor.ability.SelectItemAbility;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JSqlParser 字段操作.
 *
 * @author dongbin
 * @version 0.1 2019/10/28 17:23
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
    public void remove(Func func) throws ProcessorException {
        doRemove(func);
    }

    @Override
    public void remove(Field field) throws ProcessorException {
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
    public List<Item> list() throws ProcessorException {
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

                        selectFields.add(ConversionHelper.convertSmart(item.getExpression(), alias));

                    }

                });

            }

        }
    }

    private static class RemoveSelectVisitorImpl extends SelectVisitorAdapter {

        private String targetFieldSql;

        public RemoveSelectVisitorImpl(String targetFieldSql) {
            this.targetFieldSql = targetFieldSql;
        }

        @Override
        public void visit(PlainSelect plainSelect) {
            List<SelectItem> selectItems = plainSelect.getSelectItems();
            if (selectItems != null) {
                List<SelectItem> newSelectItems = selectItems.stream()
                    .filter(s -> !s.toString().equals(targetFieldSql)).collect(Collectors.toList());
                plainSelect.setSelectItems(newSelectItems);
            }
        }

        @Override
        public void visit(SetOperationList setOpList) {
            final int first = 0; // 如果有子表或者 union,只处理首条语句.
            visit((PlainSelect) setOpList.getSelects().get(first));
        }
    }

}
