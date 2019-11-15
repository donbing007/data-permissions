package com.xforceplus.ultraman.permissions.rule.check.insert;

import com.xforceplus.ultraman.perissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.processor.InsertSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.InsertItemAbility;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        // 只需要 key value 的值无关紧要.
        Map<String, Object> ruleTable =
            context.getSercher().searchFieldRule(
                context.authorization(), from.getTable()).stream().collect(
                Collectors.toMap(FieldRule::getField, FieldRule::getEntity));

        List<Field> fields = insertItemAbility.list();

        for (Field field : fields) {
            if (!ruleTable.containsKey(field.getName())) {
                context.refused("No field \"" + field.toSqlString() + "\" permission.");
                return;
            }
        }

    }


}
