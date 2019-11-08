package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor;

import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability.*;
import com.xforceplus.ultraman.permissions.sql.processor.UpdateSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.*;
import net.sf.jsqlparser.statement.Statement;

/**
 * update
 * @version 0.1 2019/10/25 18:55
 * @auth dongbin
 * @since 1.8
 */
public class JUpdateSqlProcessor extends JSqlProcessor implements UpdateSqlProcessor {
    public JUpdateSqlProcessor(Statement statement) {
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
    public UpdateSetAbility buildUpdateSetAbility() {
        return new JSqlParserUpdateSetAbility(getStatement());
    }

    @Override
    public FieldFromAbility buildFieldFromAbility() {
        return new JSqlParserUpdateFieldFromAbility(getStatement());
    }

    @Override
    public SubSqlAbility buildSubSqlAbility() {
        return new JSqlParserSubSqlAbility(getStatement());
    }

}
