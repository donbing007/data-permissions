package com.xforceplus.ultraman.permissions.rule.check.update;

import com.xforceplus.ultraman.perissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.define.UpdateSet;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitorAdapter;
import com.xforceplus.ultraman.permissions.sql.processor.UpdateSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.UpdateSetAbility;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查 update set 字段是否有权限.
 * @version 0.1 2019/11/12 16:52
 * @auth dongbin
 * @since 1.8
 */
public class UpdateSetFieldChecker extends AbstractTypeSafeChecker {

    public UpdateSetFieldChecker() {
        super(SqlType.UPDATE);
    }

    @Override
    protected void checkTypeSafe(Context context) {
        Sql sql = context.sql();

        if (SqlType.UPDATE != sql.type()) {
            return;
        }

        sql.visit(new SqlProcessorVisitorAdapter() {
            @Override
            public void visit(UpdateSqlProcessor processor) {
                // 查找当前表的允许字段列表.
                FromAbility fromAbility = processor.buildFromAbility();
                Map<From, List<FieldRule>> fieldRules = new HashMap();
                for (From from : fromAbility.list()) {
                    fieldRules.put(from, context.getSercher().searchFieldRule(context.authorization(), from.getTable()));
                }

                UpdateSetAbility updateSetAbility = processor.buildUpdateSetAbility();
                FieldFromAbility fieldFromAbility = processor.buildFieldFromAbility();
                List<UpdateSet> updateSets = updateSetAbility.list();

                List<AbstractMap.SimpleEntry<Field, From>> sourceFroms;
                for (UpdateSet updateSet : updateSets) {
                    sourceFroms = fieldFromAbility.searchRealTableName(updateSet.getField());
                    for (AbstractMap.SimpleEntry<Field, From> entry : sourceFroms) {
                        doCheck(fieldRules, entry.getValue(), entry.getKey(), context);
                    }
                }
            }
        });
    }

    private void doCheck(Map<From, List<FieldRule>> fieldRules, From from, Field field, Context context) {
        List<FieldRule> rules = fieldRules.get(from);
        if (rules == null) {
            context.refused("No field \"" + field.toSqlString() + "\" permission.");
        }

        for (FieldRule rule : rules) {
            if (rule.getField().equals(field.getName())) {
                return;
            }
        }

        context.refused("No field \"" + field.toSqlString() + "\" permission.");
    }
}
