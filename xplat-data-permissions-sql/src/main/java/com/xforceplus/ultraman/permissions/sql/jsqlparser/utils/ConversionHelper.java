package com.xforceplus.ultraman.permissions.sql.jsqlparser.utils;

import com.xforceplus.ultraman.permissions.sql.define.*;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.Arithmeitc;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.ArithmeticSymbol;
import com.xforceplus.ultraman.permissions.sql.define.values.UnknownValue;
import com.xforceplus.ultraman.permissions.sql.define.values.Value;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version 0.1 2019/10/30 10:49
 * @auth dongbin
 * @since 1.8
 */
public class ConversionHelper {

    private ConversionHelper() {
    }

    public static Arithmeitc convertArithmeitc(Expression expr) {
        return convertArithmeitc(expr, null);
    }

    public static Arithmeitc convertArithmeitc(Expression expr, Alias alias) {
        if (ValueHelper.isArithmeticExpr(expr)) {

            BinaryExpression binaryExpression = (BinaryExpression) expr;
            Expression l = binaryExpression.getLeftExpression();
            Expression r = binaryExpression.getRightExpression();

            Item left = convertSmart(l);
            Item right = convertSmart(r);

            ArithmeticSymbol symbol = ArithmeticSymbol.getInstance(binaryExpression.getStringExpression());

            return new Arithmeitc(left, right, symbol, convert(alias));
        }

        return null;
    }

    public static Item convertSmart(Expression expr) {
        return convertSmart(expr, null);
    }

    public static Item convertSmart(Expression expr, Alias alias) {
        if (ValueHelper.isArithmeticExpr(expr)) {

            return convertArithmeitc(expr, alias);

        } else if (ValueHelper.isValueExpr(expr)) {

            return convertValue(expr);

        } else if (Column.class.isInstance(expr)) {

            return convert((Column) expr, alias);

        } else if (Function.class.isInstance(expr)) {

            return convert((Function) expr, alias);

        } else if (Table.class.isInstance(expr)) {

            return convert((Table) expr);

        } else if (TimeKeyExpression.class.isInstance(expr)) {

            return new Func(((TimeKeyExpression) expr).getStringValue(), convert(alias));

        } else if (ValueHelper.isParenthesis(expr)) {

            return new Parentheses(
                convertSmart(((Parenthesis) expr).getExpression()),
                convert(alias)
            );

        } else if (AnyComparisonExpression.class.isInstance(expr)) {

            AnyComparisonExpression any = (AnyComparisonExpression) expr;
            return new Func(any.getAnyType().name(),
                Arrays.asList(
                    new com.xforceplus.ultraman.permissions.sql.define.values.StringValue(
                        ((AnyComparisonExpression) expr).getSubSelect().getSelectBody().toString()
                    )
                )
            );

        } else {

            return UnknownValue.getInstance(expr.toString());

        }
    }

    /**
     * 转换值类型.
     *
     * @param expr jsql的值类型.
     * @return 本地定义值.
     */
    public static Value convertValue(Expression expr) {
        if (ValueHelper.isValueExpr(expr)) {
            if (LongValue.class.isInstance(expr)) {

                return new com.xforceplus.ultraman.permissions.sql.define.values.LongValue(((LongValue) expr).getValue());

            } else if (DoubleValue.class.isInstance(expr)) {

                return new com.xforceplus.ultraman.permissions.sql.define.values.DoubleValue(((DoubleValue) expr).getValue());

            } else if (StringValue.class.isInstance(expr)) {

                return new com.xforceplus.ultraman.permissions.sql.define.values.StringValue(((StringValue) expr).getValue());

            } else if (NullValue.class.isInstance(expr)) {

                return com.xforceplus.ultraman.permissions.sql.define.values.NullValue.getInstance();

            }
        }

        return UnknownValue.getInstance(expr.toString());
    }


    public static Field convert(Column column) {
        return convert(column, null);
    }

    public static Field convert(Column column, Alias alias) {
        String tableName = null;
        if (column.getTable() != null) {
            tableName = column.getTable().getName();
        }

        return new Field(tableName, column.getColumnName(), convert(alias));
    }

    public static Func convert(TimeKeyExpression expr) {
        return new Func(expr.getStringValue(), null, null);
    }

    public static Func convert(TimeKeyExpression expr, Alias alias) {
        return new Func(expr.getStringValue(), convert(alias));
    }

    public static Func convert(Function function) {
        return convert(function, null);
    }

    public static Func convert(Function function, Alias alias) {
        ExpressionList parameterList = function.getParameters();

        if (function.isAllColumns()) {
            return new Func(function.getName(), Arrays.asList(new Field("*")), convert(alias));
        }

        List<Expression> parameters = null;
        if (parameterList != null) {
            parameters = parameterList.getExpressions();
        }

        List<Item> paramItemList = new ArrayList<>(parameters.size());
        if (parameters != null && !parameters.isEmpty()) {
            for (Expression expr : parameters) {
                conversionFuncParam(paramItemList, expr);
            }
        }

        return new Func(function.getName(), paramItemList, convert(alias));
    }

    public static From convert(Table table) {
        if (table == null) {
            return null;
        } else {
            return new From(table.getName(), ConversionHelper.convert(table.getAlias()));
        }
    }

    public static com.xforceplus.ultraman.permissions.sql.define.Alias convert(Alias alias) {
        if (alias == null) {
            return null;
        } else {
            return new com.xforceplus.ultraman.permissions.sql.define.Alias(alias.getName(), alias.isUseAs());
        }
    }

    private static void conversionFuncParam(List<Item> paramItemList, Expression expr) {
        if (Column.class.isInstance(expr)) {

            paramItemList.add(ConversionHelper.convert((Column) expr));

        } else if (ValueHelper.isValueExpr(expr)) {

            paramItemList.add(convertValue(expr));
        }
    }

}
