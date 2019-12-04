package com.xforceplus.ultraman.permissions.rule.check.select;

import com.xforceplus.ultraman.permissions.rule.check.AbstractTypeSafeChecker;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.rule.utils.FieldCheckHelper;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.SelectItemAbility;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    }

    private void checkSelectItem(SelectSqlProcessor selectSqlProcessor, Context context) {

        SelectItemAbility selectItemAbility = selectSqlProcessor.buildSelectItemAbility();
        List<Item> items = selectItemAbility.list();
        // 找出所有的 Field
        Map<Field, Item> fieldMap = new HashMap();
        items.stream().forEach(item -> {
            FieldCheckHelper.fillField(item, item, fieldMap);
        });

        Collection<Field> hideField = FieldCheckHelper.checkFieldsRule(
            selectSqlProcessor.buildFieldFromAbility(),
            context.authorization(),
            fieldMap.keySet(),
            context.getSercher());

        for (Field field : hideField) {
            context.black(fieldMap.get(field));
        }

        if (context.blackSize() == items.size()) {
            context.refused("None of the fields are viewed with permission.");
        }
    }
}
