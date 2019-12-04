package com.xforceplus.ultraman.permissions.rule.check.insert;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.rule.utils.FieldCheckHelper;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.processor.InsertSqlProcessor;

import java.util.Collection;
import java.util.List;

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
        Authorizations authorizations = context.authorization();
        Searcher searcher = context.getSercher();
        List<Field> fields = processor.buildInsertItemAbility().list();

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
