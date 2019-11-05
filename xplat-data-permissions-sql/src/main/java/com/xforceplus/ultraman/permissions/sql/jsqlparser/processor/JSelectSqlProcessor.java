package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor;

import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability.*;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.*;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;

/**
 * select
 * @version 0.1 2019/10/25 18:50
 * @auth dongbin
 * @since 1.8
 */
public class JSelectSqlProcessor extends JSqlProcessor implements SelectSqlProcessor {

    public JSelectSqlProcessor(Statement statement) {
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
    public SelectItemAbility buildSelectItemAbility() {
        return new JSqlParserSelectItemAbility(getStatement());
    }

    @Override
    public SubSqlAbility buildSubSqlAbility() {
        return new JSqlParserSubSqlAbility((Select) getStatement());
    }

    @Override
    public FieldFromAbility buildFieldFromAbility() {
        return new JSqlParserSelectFieldFromAbility(getStatement());
    }


}
