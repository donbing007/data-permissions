package com.xforceplus.ultraman.permissions.rule.check.common;

import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.rule.utils.FieldCheckHelper;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.Condition;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.JSubSelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.DeleteSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitorAdapter;
import com.xforceplus.ultraman.permissions.sql.processor.UpdateSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.ConditionAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;
import com.xforceplus.ultraman.permissions.sql.utils.SubSqlIterator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 条件字段检查.
 * @author dongbin
 * @version 0.1 2019/12/4 17:08
 * @since 1.8
 */
public class ConditionsFieldChecker extends AbstractTypeSafeChecker {

    public ConditionsFieldChecker() {
        super(new SqlType[]{
            SqlType.UPDATE,
            SqlType.DELETE,
            SqlType.SELECT,
        });
    }

    @Override
    protected void checkTypeSafe(Context context) {
        Sql sql = context.sql();

        sql.visit(new SqlProcessorVisitorAdapter() {
            @Override
            public void visit(SelectSqlProcessor processor) {
                checkCondition(processor.buildConditionAbility(), processor.buildFieldFromAbility(), context);

                if (!context.isRefused()) {
                    SubSqlIterator subSqlIterator = new SubSqlIterator(processor.buildSubSqlAbility());
                    Sql subSql;
                    JSubSelectSqlProcessor subSelectProcessor;
                    while(subSqlIterator.hasNext()) {
                        subSql = subSqlIterator.next();
                        subSelectProcessor = (JSubSelectSqlProcessor) subSql.buildProcessor();

                        checkCondition(
                            subSelectProcessor.buildConditionAbility(),
                            subSelectProcessor.buildFieldFromAbility(), context);

                        if (context.isRefused()) {
                            return;
                        }
                    }
                }
            }

            @Override
            public void visit(DeleteSqlProcessor processor) {
                checkCondition(processor.buildConditionAbility(), processor.buildFieldFromAbility(), context);
            }

            @Override
            public void visit(UpdateSqlProcessor processor) {
                checkCondition(processor.buildConditionAbility(), processor.buildFieldFromAbility(), context);
            }
        });
    }

    private void doCheck(Sql sql) {

    }

    //任何一个字段没有权限都将拒绝执行.
    private void checkCondition(ConditionAbility conditionAbility, FieldFromAbility fieldFromAbility, Context context) {
        List<Condition> conditions = conditionAbility.list();

        Map<Field, Item> fieldMap = new HashMap<>();
        conditions.stream().forEach(condition -> {
            FieldCheckHelper.fillField(condition.getColumn(), condition.getColumn(), fieldMap);
            condition.getValues().stream().forEach(v -> {
                FieldCheckHelper.fillField(v, v, fieldMap);
            });
        });

        Collection<Field> noRuleFields =
            FieldCheckHelper.checkFieldsRule(
                fieldFromAbility, context.authorization(), fieldMap.keySet(), context.getSercher());

        if (!noRuleFields.isEmpty()) {
            Set<Item> filterItems = noRuleFields.stream().map(f -> fieldMap.get(f)).collect(Collectors.toSet());
            StringBuilder buff = new StringBuilder();
            buff.append("Item [");
            for (Item item : filterItems) {
                buff.append(item.toSqlString()).append(",");
            }
            buff.append("] has no permissions.");
            context.refused(buff.toString());
        }
    }
}
