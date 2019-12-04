package com.xforceplus.ultraman.permissions.rule.check.update;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.rule.utils.FieldCheckHelper;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.define.UpdateSet;
import com.xforceplus.ultraman.permissions.sql.processor.UpdateSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.UpdateSetAbility;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 检查 update set 字段是否有权限.
 *
 * @author dongbin
 * @version 0.1 2019/11/12 16:52
 * @since 1.8
 */
public class UpdateSetFieldChecker extends AbstractTypeSafeChecker {

    public UpdateSetFieldChecker() {
        super(SqlType.UPDATE);
    }

    @Override
    protected void checkTypeSafe(Context context) {
        Sql sql = context.sql();

        UpdateSqlProcessor processor = (UpdateSqlProcessor) sql.buildProcessor();

        Authorizations authorizations = context.authorization();
        Searcher searcher = context.getSercher();

        UpdateSetAbility updateSetAbility = processor.buildUpdateSetAbility();
        List<UpdateSet> updateSets = updateSetAbility.list();
        List<Field> fields = updateSets.parallelStream().map(u -> u.getField()).collect(Collectors.toList());

        Collection<Field> noRuleFields =
            FieldCheckHelper.checkFieldsRule(processor.buildFieldFromAbility(), authorizations, fields, searcher);

        if (!noRuleFields.isEmpty()) {
            StringBuilder buff = new StringBuilder();
            buff.append("Field [");
            for (Field f : noRuleFields) {
                buff.append(f.toSqlString()).append(",");
            }
            buff.append("] has no permissions.");
            context.refused(buff.toString());
        }
    }
}
