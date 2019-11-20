package com.xforceplus.ultraman.permissions.rule.check.insert;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.rule.utils.FieldCheckHelper;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.processor.InsertSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.InsertItemAbility;

import java.util.List;
import java.util.Set;

/**
 * insert 语句的字段权限检查.
 *
 * @author dongbin
 * @version 0.1 2019/11/12 19:12
 * @since 1.8
 */
public class InsertFieldChecker extends AbstractTypeSafeChecker {

    public InsertFieldChecker() {
        super(SqlType.INSERT);
    }

    @Override
    protected void checkTypeSafe(Context context) {
        Sql sql = context.sql();

        InsertSqlProcessor processor = (InsertSqlProcessor) sql.buildProcessor();
        FromAbility fromAbility = processor.buildFromAbility();
        List<From> froms = fromAbility.list();

        // insert 只有一个表
        From from = froms.stream().findFirst().get();

        InsertItemAbility insertItemAbility = processor.buildInsertItemAbility();

        Set<Authorization> authorizations = context.authorization().getAuthorizations();

        // 当前字段列表
        List<Field> fields = insertItemAbility.list();
        List<FieldRule> rules;
        // 多个授权信息
        for (Authorization authorization : authorizations) {

            rules = context.getSercher().searchFieldRule(authorization, from.getTable());
            for (Field currentField : fields) {
                if (!FieldCheckHelper.checkRule(rules, currentField)) {
                    context.refused("No field \"" + currentField.toSqlString() + "\" permission.");
                    return;
                }
            }
        }

    }


}
