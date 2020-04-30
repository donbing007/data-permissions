package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ConversionHelper;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;

import java.util.*;

/**
 * 处理 delete 语句的字段来源查找.
 * 只会处理 where 子句.
 *
 * @author dongbin
 * @version 0.1 2019/11/4 17:05
 * @since 1.8
 */
public class JSqlParserDeleteFieldFromAbility extends AbstractJSqlParserHandler implements FieldFromAbility {

    public JSqlParserDeleteFieldFromAbility(Statement statement) {
        super(statement, Delete.class);
    }

    @Override
    public List<Map.Entry<Field, From>> searchRealTableName(Item item) throws ProcessorException {
        return searchRealTableName((Field) item);
    }

    private List<Map.Entry<Field, From>> searchRealTableName(Field field) {

        Expression where = getDelete().getWhere();
        if (where == null) {
            return Collections.emptyList();
        }

        if (exists(field, where)) {
            return Arrays.asList(new AbstractMap.SimpleEntry(field, ConversionHelper.convert(getDelete().getTable())));
        } else {
            return Collections.emptyList();
        }
    }

    private boolean exists(Field field, Expression expr) {
        if (BinaryExpression.class.isInstance(expr)) {

            boolean result = exists(field, ((BinaryExpression) expr).getLeftExpression());
            if (!result) {
                result = exists(field, ((BinaryExpression) expr).getRightExpression());
            }

            return result;

        } else if (ComparisonOperator.class.isInstance(expr)) {

            return exists(field, ((ComparisonOperator) expr).getLeftExpression());

        } else if (Column.class.isInstance(expr)) {

            return expr.toString().equals(field.toSqlString());

        }

        return false;
    }
}
