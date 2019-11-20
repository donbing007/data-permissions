package com.xforceplus.ultraman.permissions.rule.check.select;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.rule.utils.FieldCheckHelper;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.*;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.ConditionAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.SelectItemAbility;

import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * select 语句字段检查.
 *
 * @author dongbin
 * @version 0.1 2019/11/6 15:24
 * @since 1.8
 */
public class SelectFieldChecker extends AbstractTypeSafeChecker {

    public SelectFieldChecker() {
        super(SqlType.SELECT);
    }

    @Override
    protected void checkTypeSafe(Context context) {
        Sql sql = context.sql();

        SelectSqlProcessor processor = (SelectSqlProcessor) sql.buildProcessor();

        checkSelectItem(processor, context);

        if (!context.isRefused()) {
            checkCondition(processor, context);
        }
    }

    private void checkSelectItem(SelectSqlProcessor selectSqlProcessor, Context context) {

        SelectItemAbility selectItemAbility = selectSqlProcessor.buildSelectItemAbility();

        List<Item> items = selectItemAbility.list();
        items = items.stream().filter(item -> {
            // 过滤掉 * 号字段.
            if (Field.class.isInstance(item) && Field.getAllField().equals(item)) {
                return false;
            } else {
                return true;
            }
        }).collect(Collectors.toList());

        items.stream().forEach(item -> {

            if (needHide(selectSqlProcessor, item, context)) {
                context.black(item);
            }
        });

        if (context.blackSize() == items.size()) {
            context.refused("None of the fields are viewed with permission.");
        }
    }

    //任何一个字段没有权限都将拒绝执行.
    private void checkCondition(SelectSqlProcessor processor, Context context) {
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
    private boolean needHide(SelectSqlProcessor selectSqlProcessor, Item item, Context context) {
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

                for (Authorization authorization : context.authorization().getAuthorizations()) {
                    rules = context.getSercher().searchFieldRule(authorization, from.getTable());
                    if (!FieldCheckHelper.checkRule(rules, field)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

}
