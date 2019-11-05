package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.define.Func;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ConversionHelper;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.util.*;

/**
 * 查找指定字段的属于的 From .
 * @version 0.1 2019/11/1 15:26
 * @auth dongbin
 * @since 1.8
 */
public class JSqlParserSelectFieldFromAbility extends AbstractJSqlParserHandler implements FieldFromAbility {

    public JSqlParserSelectFieldFromAbility(Statement statement) {
        super(statement, Select.class);
    }

    public JSqlParserSelectFieldFromAbility(PlainSelect plainSelect) {
        super(plainSelect);
    }

    @Override
    public List<AbstractMap.SimpleEntry<Field, From>> searchRealTableName(Field field) {
        if (isSubSelect()) {

            return doSearchFromPlainSelect(field, getSubSelect(), true);

        } else {

            return doSearchFromPlainSelect(field, (PlainSelect) getSelect().getSelectBody(), true);
        }
    }

    private List<AbstractMap.SimpleEntry<Field, From>> doSearchFromPlainSelect(Field field, PlainSelect plainSelect, boolean equalsSubAlisa) {
        List<AbstractMap.SimpleEntry<Field, From>> froms = doSearchFromItem(field, plainSelect.getFromItem(), equalsSubAlisa);

        if (froms.isEmpty()) {
            froms = doSearchJoins(field, plainSelect.getJoins(), equalsSubAlisa);
        }

        return froms;
    }

    private List<AbstractMap.SimpleEntry<Field, From>> doSearchFromItem(Field field, FromItem fromItem, boolean equalsSubAlisa) {

        if (fromItem == null) {
            return null;
        }


        if (Table.class.isInstance(fromItem)) {
            Table table = (Table) fromItem;

            if (checkTable(field, table)) {
                return Arrays.asList(new AbstractMap.SimpleEntry(field, ConversionHelper.convert(table)));
            }


        } else if (SubSelect.class.isInstance(fromItem)) {

            /**
             * 处理类似语句.
             *
             * select t.id from (
             *     select ta2.id from
             *        (select id from t1) ta2
             *     ) ta1
             */

            SubSelect subSelect = (SubSelect) fromItem;

            /**
             *  当前查找的字段替换成子查询中的返回段.
             *  select t1.id from (select t2.id from t2) t1;
             *  在子查询中,目标字段 t1.id 会被替换成 t2.id.
             *  如果映射结果是一个函数,那么函数的参数中出现的字段都将被查询.
             *  select t1.total from (select func(t2.c1, t2.c2) total from t2) t1;
             *  t.total 会被映射成 t2.c1 和 c2.c2 的组合.
             */
            Item mappingItem = findMappingField(field, (PlainSelect) subSelect.getSelectBody());
            if (mappingItem != null) {

                if (Field.class.isInstance(mappingItem)) {

                    /**
                     * from 子查询必须有别名.从这里进入表示最终来源表一定在子查询 table 中或者子查询的子查询中.
                     */
                    if (equalsSubAlisa) {
                        if (!subSelect.getAlias().getName().equals(field.getRef())) {
                            // 没有匹配的表,有可能在 join 中如果存在.
                            return Collections.emptyList();
                        }
                    }

                    return doSearchFromPlainSelect((Field) mappingItem, (PlainSelect) subSelect.getSelectBody(), false);

                } else if (Func.class.isInstance(mappingItem)) {
                    Func f = (Func) mappingItem;
                    List<Item> parameters = f.getParameters();
                    Field parameterField;
                    List<AbstractMap.SimpleEntry<Field, From>> froms = new ArrayList();
                    if (parameters != null && !parameters.isEmpty()) {
                        for (Item parameter : parameters) {
                            if (Field.class.isInstance(parameter)) {

                                parameterField = (Field) parameter;

                                froms.addAll(doSearchFromPlainSelect(parameterField, (PlainSelect) subSelect.getSelectBody(), true));

                            }
                        }
                    }

                    if (froms.isEmpty()) {
                        return Collections.emptyList();
                    } else {
                        return froms;
                    }
                }

            }


        } else if (SubJoin.class.isInstance(fromItem)) {
            // nothing...
        }

        return Collections.emptyList();

    }

    private List<AbstractMap.SimpleEntry<Field, From>> doSearchJoins(Field field, List<Join> joins, boolean equalsSubAlisa) {
        if (joins == null) {
            return Collections.emptyList();
        }

        List<AbstractMap.SimpleEntry<Field, From>> froms;
        for (Join join : joins) {
            froms = doSearchFromItem(field, join.getRightItem(), equalsSubAlisa);

            if (!froms.isEmpty()) {
                return froms;
            }
        }

        return Collections.emptyList();
    }

    private Item findMappingField(Field source, PlainSelect select) {
        JSqlParserSelectItemAbility selectItemHandler = new JSqlParserSelectItemAbility(select);
        List<Item> selectItems = selectItemHandler.list();
        Field targetField;
        Func targetFunc;
        for (Item item : selectItems) {
            if (Field.class.isInstance(item)) {
                targetField = (Field) item;
                if (isTheSameField(source, targetField)) {
                    return targetField;
                }
            } else if (Func.class.isInstance(item)) {
                targetFunc = (Func) item;
                if (isTheSameField(source, targetFunc)) {
                    return targetFunc;
                }
            }
        }

        return null;
    }

    private boolean isTheSameField(Field source, Func target) {
        if (target.getAlias() != null) {
            return source.getName().equals(target.getAlias().getName());
        } else {
            return false;
        }
    }

    private boolean isTheSameField(Field source, Field target) {
        if (target.getAlias() != null) {
            return source.getName().equals(target.getAlias().getName());
        } else {
            return source.getName().equals(target.getName());
        }
    }


    // 判断字段是否属于当前表.
    private boolean checkTable(Field field, Table table) {
        if (table.getAlias() != null) {
            // 别名符合.
            return table.getAlias().getName().equals(field.getRef());

        } else if (table.getName().equals(field.getRef())) {
            // 表名符合.
            return true;
        }


        return false;
    }
}
