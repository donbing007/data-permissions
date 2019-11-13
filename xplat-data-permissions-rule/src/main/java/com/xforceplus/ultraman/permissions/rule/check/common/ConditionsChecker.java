package com.xforceplus.ultraman.permissions.rule.check.common;

import com.xforceplus.ultraman.perissions.pojo.rule.DataRule;
import com.xforceplus.ultraman.perissions.pojo.rule.DataRuleCondition;
import com.xforceplus.ultraman.perissions.pojo.rule.RuleConditionRelationship;
import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.rule.convert.condition.operator.ConditionOperationConvertingFactory;
import com.xforceplus.ultraman.permissions.rule.convert.condition.value.ConditionValueConvertingFactory;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.*;
import com.xforceplus.ultraman.permissions.sql.define.relationship.And;
import com.xforceplus.ultraman.permissions.sql.define.relationship.Or;
import com.xforceplus.ultraman.permissions.sql.define.relationship.Relationship;
import com.xforceplus.ultraman.permissions.sql.processor.DeleteSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.UpdateSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.ConditionAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FromAbility;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * 语句的条件检查,会修改原始SQL.
 *
 * @version 0.1 2019/11/8 17:12
 * @auth dongbin
 * @since 1.8
 */
public class ConditionsChecker extends AbstractTypeSafeChecker {

    public ConditionsChecker() {
        super(new SqlType[] {
            SqlType.UPDATE,
            SqlType.DELETE,
            SqlType.SELECT,
        });
    }

    @Override
    protected void checkTypeSafe(Context context) {
        Sql sql = context.sql();

        switch (sql.type()) {
            case SELECT: {
                Queue<Sql> queue = new ArrayDeque<>();
                queue.add(sql);
                Sql currentSql;
                SelectSqlProcessor processor;
                while(!queue.isEmpty()) {
                    currentSql = queue.poll();

                    processor = (SelectSqlProcessor) currentSql.buildProcessor();

                    queue.addAll(processor.buildSubSqlAbility().list());

                    doCheckSelect(processor, context);
                }
                break;
            }
            case DELETE:  {
                // delete 不允许子句.

                doCheckDelete((DeleteSqlProcessor) sql.buildProcessor(),context);
                break;
            }
            case UPDATE: {
                // update 不允许子句.

                doCheckUpdate((UpdateSqlProcessor) sql.buildProcessor(), context);
                break;
            }
        }
    }

    private void doCheckDelete(DeleteSqlProcessor processor, Context context) {
        doCheck(processor.buildFromAbility(), processor.buildConditionAbility(), context);
    }

    private void doCheckUpdate(UpdateSqlProcessor processor, Context context) {
        doCheck(processor.buildFromAbility(), processor.buildConditionAbility(), context);
    }

    private void doCheckSelect(SelectSqlProcessor processor, Context context) {
        doCheck(processor.buildFromAbility(), processor.buildConditionAbility(), context);
    }

    private void doCheck(FromAbility fromAbility, ConditionAbility conditionAbility, Context context) {
        List<From> froms = fromAbility.list();
        //只保留非子 from.
        froms = froms.stream().filter(f -> !f.isSub()).collect(Collectors.toList());

        List<DataRule> rules = new ArrayList();
        Searcher searcher = context.getSercher();
        for (From from : froms) {
            rules.addAll(searcher.searchDataRule(context.authorization(), from.getTable()));
        }

        Item newConditions;
        for (DataRule rule : rules) {
            newConditions = buildConditions(froms, rule);
            if (Condition.class.isInstance(newConditions)) {
                conditionAbility.add((Condition) newConditions, Conditional.AND, true);
            } else {
                conditionAbility.add((Relationship) newConditions, Conditional.AND, true);
            }
        }
    }

    private Item buildConditions(List<From> froms, DataRule rule) {

        String ref = findFieldRef(froms, rule.getEntity());

        if (ref == null) {
            throw new IllegalArgumentException(
                String.format("The source of the field could not be found.[%s.%s]",
                    rule.getEntity(), rule.getField()));
        }

        Field field = new Field(ref, rule.getField());

        List<DataRuleCondition> conditions = rule.getConditions();

        // 表示只有一个条件,并没有两两关系.
        final int onlyOne = 1;
        if (conditions.size() == onlyOne) {

            return buildCondition(field, conditions.get(0));

        } else {

            // 先取出第一第二个,组成一个关系.
            DataRuleCondition left = conditions.get(0);
            DataRuleCondition right = conditions.get(1);
            Relationship relationship = buildRelationship(
                buildCondition(field,left), buildCondition(field, right), right.getLink());

            DataRuleCondition dataRuleCondition;
            for (int i = 2; i < conditions.size(); i++) {
                dataRuleCondition = conditions.get(i);

                relationship = buildRelationship(
                    relationship, buildCondition(field, dataRuleCondition), dataRuleCondition.getLink());
            }

            return relationship;
        }
    }

    private Condition buildCondition(Field field, DataRuleCondition rule) {
        return new Condition(
            field,
            ConditionOperationConvertingFactory.getConverting(rule.getOperation()).convert(rule.getOperation()),
            ConditionValueConvertingFactory.getConverting(rule.getType()).convert(rule));
    }

    private Relationship buildRelationship(
        Item left, Item right, RuleConditionRelationship link) {

        if (RuleConditionRelationship.AND == link) {
            return new And(left, right);
        } else if (RuleConditionRelationship.OR == link) {
            return new Or(left, right);
        }

        return null;
    }

    private String findFieldRef(List<From> froms, String entity) {
        for (From from : froms) {
            if (from.getTable().equals(entity)) {
                return decideFieldRef(from);
            }
        }

        return null;
    }

    // 决定是使用的字段引用名称.
    private String decideFieldRef(From from) {
        if (from.hasAlias()) {
            return from.getAlias().getName();
        } else {
            return from.getTable();
        }
    }
}