package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ConversionHelper;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.update.Update;

import java.text.ParseException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * update 语句的字段来源查询.
 * @version 0.1 2019/11/4 14:47
 * @auth dongbin
 * @since 1.8
 */
public class JSqlParserUpdateFieldFromAbility extends AbstractJSqlParserHandler implements FieldFromAbility {

    public JSqlParserUpdateFieldFromAbility(Statement statement) {
        super(statement, Update.class);
    }

    @Override
    public List<AbstractMap.SimpleEntry<Field, From>> searchRealTableName(Item item) throws ProcessorException {
        return searchRealTableName((Field) item);
    }

    /**
     * 处理
     *     update t inner join t2 on t.id=t2.id set t.c1 = t2.c1 where t.c1=100
     *     update t set t.c1=value where t.c1=value
     *  这样的语句.
     * 查询的字段必须是语句中出现的, 比如 t.id
     * @param field 查询的字段.
     * @return 结果.
     */
    private List<AbstractMap.SimpleEntry<Field, From>> searchRealTableName(Field field) {

        if (!exist(field)) {
            return Collections.emptyList();
        }

        Update update = getUpdate();
        if (field.getRef() == null) {
            if (noStartJoin() && noFromItem() && noJoins()) {
                return Arrays.asList(
                    new AbstractMap.SimpleEntry(field, ConversionHelper.convert(update.getTable())));
            } else {
                return Collections.emptyList();
            }
        } else {

            List<AbstractMap.SimpleEntry<Field, From>> froms = null;
            froms = doSearchFromTable(field, update.getTable());

            if (froms.isEmpty()) {

                froms = doSearchFromStartJoin(field, update.getStartJoins());

            }

            return froms;
        }
    }

    private List<AbstractMap.SimpleEntry<Field, From>> doSearchFromStartJoin(Field field, List<Join> startJoins) {
        List<AbstractMap.SimpleEntry<Field, From>> froms;
        for (Join join : startJoins) {
            froms = doSearchFromTable(field, (Table) join.getRightItem());

            if (!froms.isEmpty()) {
                return froms;
            }
        }

        return Collections.emptyList();
    }

    private List<AbstractMap.SimpleEntry<Field, From>> doSearchFromTable(Field field, Table table) {
        if (table.getAlias() != null) {
            if (table.getAlias().getName().equals(field.getRef())) {
                return Arrays.asList(new AbstractMap.SimpleEntry(field, ConversionHelper.convert(table)));
            }
        } else {
            if (table.getName().equals(field.getRef())) {
                return Arrays.asList(new AbstractMap.SimpleEntry(field, ConversionHelper.convert(table)));
            }
        }

        return Collections.emptyList();
    }

    // 判断字段是否存在语句中.忽略 where.
    private boolean exist(Field target) {
        List<Column> columns = getUpdate().getColumns();
        for (Column column : columns) {
            if (equalsFieldAndColumn(target, column)) {
                return true;
            }
        }

        List<Expression> exprs = getUpdate().getExpressions();
        for (Expression expr : exprs) {
            if (Column.class.isInstance(expr)) {
                if (equalsFieldAndColumn(target, (Column) expr)) {
                    return true;
                }
            }
        }


        return false;
    }

    private boolean noStartJoin() {
        List<Join> joins = getUpdate().getStartJoins();
        return joins == null || joins.isEmpty() ? true : false;
    }

    private boolean noFromItem() {
        return getUpdate().getFromItem() == null ? true : false;
    }

    private boolean noJoins() {
        List<Join> joins = getUpdate().getJoins();
        return joins == null || joins.isEmpty() ? true : false;
    }

    private boolean equalsFieldAndColumn(Field field, Column column) {
        return field.toSqlString().equals(column.toString());
    }

}
