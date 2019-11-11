package com.xforceplus.ultraman.permissions.rule.check.select;

import com.xforceplus.ultraman.perissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.rule.check.Checker;
import com.xforceplus.ultraman.permissions.rule.context.CheckContext;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.Condition;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitorAdapter;
import com.xforceplus.ultraman.permissions.sql.processor.ability.ConditionAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.SelectItemAbility;

import java.util.AbstractMap;
import java.util.List;

/**
 * select 语句字段检查.
 *
 * @version 0.1 2019/11/6 15:24
 * @auth dongbin
 * @since 1.8
 */
public class SelectFieldChecker implements Checker {

    @Override
    public void check(CheckContext context) {
        Sql sql = context.sql();

        sql.visit(new SqlProcessorVisitorAdapter() {
            @Override
            public void visit(SelectSqlProcessor processor) {


                checkSelectItem(processor, context);

                if (!context.isRefused()) {
                    checkCondition(processor, context);
                }
            }
        });

    }

    private void checkSelectItem(SelectSqlProcessor selectSqlProcessor, CheckContext context) {

        SelectItemAbility selectItemAbility = selectSqlProcessor.buildSelectItemAbility();

        List<Item> items = selectItemAbility.list();
        items.stream().filter(i -> {
            if (Field.class.isInstance(i) && Field.getAllField().equals(i)) {
                return false;
            } else {
                return true;
            }
        }).forEach(i -> {

            if (needHide(selectSqlProcessor, i, context)) {
                context.black(i);
            }
        });

        if (context.blackSize() == items.size()) {
            context.refused("None of the fields are viewed with permission.");
        }
    }

    //任何一个字段没有权限都将拒绝执行.
    private void checkCondition(SelectSqlProcessor processor, CheckContext context) {
        ConditionAbility conditionAbility = processor.buildConditionAbility();
        List<Condition> conditions = conditionAbility.list();

        for (Condition c : conditions) {
            if (needHide(processor, c.getColumn(), context)) {
                context.refused(String.format("%s unauthorized!", c.getColumn().toSqlString()));
                return;
            }

            for (Item item : c.getValues()) {
                if (needHide(processor, item, context)) {
                    context.refused(String.format("%s unauthorized!", item.toSqlString()));
                    return;
                }
            }
        }
    }

    /**
     * 判断是否需要隐藏的字段.  true 需要隐藏,false 不需要.
     */
    private boolean needHide(SelectSqlProcessor selectSqlProcessor, Item item, CheckContext context) {
        FieldFromAbility fieldFromAbility = selectSqlProcessor.buildFieldFromAbility();
        List<AbstractMap.SimpleEntry<Field, From>> froms = fieldFromAbility.searchRealTableName(item);

        if (froms.isEmpty()) {
            // 表示不在任何表,不隐藏.
            return false;
        } else {

            Field field;
            From from;
            List<FieldRule> rules;
            for (AbstractMap.SimpleEntry<Field, From> entry : froms) {
                field = entry.getKey();
                from = entry.getValue();

                rules = context.getSercher().searchFieldRule(context.authorization(), from.getTable());
                if (!checkRule(rules, field)) {
                    return true;
                }
            }

            return false;
        }
    }

    // true 在规则中,false 不在.
    private boolean checkRule(List<FieldRule> rules, Field field) {
        for (FieldRule rule : rules) {
            if (rule.getField().equals(field.getName())) {
                return true;
            }
        }

        return false;
    }

}
