package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.UpdateSet;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ConversionHelper;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ValueHelper;
import com.xforceplus.ultraman.permissions.sql.processor.ability.UpdateSetAbility;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * update set handler.
 * @version 0.1 2019/10/30 10:13
 * @auth dongbin
 * @since 1.8
 */
public class JSqlParserUpdateSetAbility extends AbstractJSqlParserHandler implements UpdateSetAbility {


    public JSqlParserUpdateSetAbility(Statement statement) {
        super(statement, Update.class);
    }

    @Override
    public List<UpdateSet> list() {
        Update update = getUpdate();
        List<Column> columns = update.getColumns();
        if (columns == null || columns.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        List<Expression> exprs = update.getExpressions();

        Field field = null;
        Expression expr = null;
        List<UpdateSet> updateSets = new ArrayList<>(columns.size());
        for (int i = 0; i < columns.size(); i++) {
            field = ConversionHelper.convert(columns.get(i));

            expr = exprs.get(i);

            if (ValueHelper.isValueExpr(expr)) {

                updateSets.add(new UpdateSet(field, ConversionHelper.convertValue(expr)));

            } else if (Function.class.isInstance(expr)){


                updateSets.add(new UpdateSet(field, ConversionHelper.convert((Function) expr)));
            } else {
                continue;
            }
        }

        return updateSets;
    }

}
