package com.xforceplus.ultraman.permissions.sql.processor;

import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FromAbility;

/**
 * 表示当前无法进行操作.
 *
 * @author dongbin
 * @version 0.1 2019/10/31 13:54
 * @since 1.8
 */
public class UnableOperateSqlProcessor implements SqlProcessor {

    private static final UnableOperateSqlProcessor INSTANCE = new UnableOperateSqlProcessor();

    public static UnableOperateSqlProcessor getInstance() {
        return INSTANCE;
    }

    @Override
    public FromAbility buildFromAbility() {
        return null;
    }

    @Override
    public FieldFromAbility buildFieldFromAbility() {
        return null;
    }
}
