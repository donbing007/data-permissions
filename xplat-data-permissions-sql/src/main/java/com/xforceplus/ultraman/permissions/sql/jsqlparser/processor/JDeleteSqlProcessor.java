package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor;

import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability.JSqlParserConditionAbility;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability.JSqlParserDeleteFieldFromAbility;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability.JSqlParserFromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.DeleteSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.ConditionAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FromAbility;
import net.sf.jsqlparser.statement.Statement;

/**
 * delete
 * @version 0.1 2019/10/25 18:56
 * @auth dongbin
 * @since 1.8
 */
public class JDeleteSqlProcessor extends JSqlProcessor implements DeleteSqlProcessor {
    public JDeleteSqlProcessor(Statement statement) {
        super(statement);
    }

    @Override
    public ConditionAbility buildConditionAbility() {
        return new JSqlParserConditionAbility(getStatement());
    }

    @Override
    public FromAbility buildFromAbility() {
        return new JSqlParserFromAbility(getStatement());
    }

    @Override
    public FieldFromAbility buildFieldFromAbility() {
        return new JSqlParserDeleteFieldFromAbility(getStatement());
    }
}
