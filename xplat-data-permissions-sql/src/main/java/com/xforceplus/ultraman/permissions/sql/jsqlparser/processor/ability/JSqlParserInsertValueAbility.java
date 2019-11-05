package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Func;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.values.ArithmeticValue;
import com.xforceplus.ultraman.permissions.sql.define.values.UnknownValue;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ConversionHelper;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ValueHelper;
import com.xforceplus.ultraman.permissions.sql.processor.ability.InsertValueAbility;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.TimeKeyExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;

import java.util.ArrayList;
import java.util.List;

/**
 * insert values value 子句.
 * @version 0.1 2019/10/30 17:24
 * @auth dongbin
 * @since 1.8
 */
public class JSqlParserInsertValueAbility extends AbstractJSqlParserHandler implements InsertValueAbility {

    public JSqlParserInsertValueAbility(Statement statement) {
        super(statement, Insert.class);
    }

    @Override
    public List<Item> list(int index) {
        List<Item> items = new ArrayList();
        getInsert().getItemsList().accept(new ItemsListVisitorAdapter() {
            @Override
            public void visit(ExpressionList expressionList) {
                if (index == 0) {
                    List<Expression> exprList = expressionList.getExpressions();
                    if (exprList != null) {
                        for (Expression expr : exprList) {
                            items.add(toItem(expr));
                        }
                    }
                }
            }

            @Override
            public void visit(MultiExpressionList multiExprList) {
                List<ExpressionList> expressionLists = multiExprList.getExprList();
                ExpressionList exprList = expressionLists.get(index);
                if (exprList != null) {
                    List<Expression> exprs = exprList.getExpressions();
                    for (Expression expr : exprs) {
                        items.add(toItem(expr));
                    }
                }
            }
        });

        return items;
    }

    @Override
    public int size() {
        ItemsList itemList = getInsert().getItemsList();
        if (ExpressionList.class.isInstance(itemList)) {

            return 1;

        } else if (MultiExpressionList.class.isInstance(itemList)) {

            MultiExpressionList mList = (MultiExpressionList) itemList;
            return mList.getExprList().size();

        } else {
            return 0;
        }
    }

    private Item toItem(Expression expr) {
        if (ValueHelper.isValueExpr(expr)) {

            return ConversionHelper.convertValue(expr);

        } else if (ValueHelper.isArithmeticExpr(expr)) {

            return new ArithmeticValue(expr.toString());

        } else if (Function.class.isInstance(expr)) {

            return ConversionHelper.convert((Function)expr);

        } else if (TimeKeyExpression.class.isInstance(expr)) {

            return new Func(((TimeKeyExpression) expr).getStringValue());

        } else {
            return UnknownValue.getInstance(expr.toString());
        }
    }

}
