package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor;

import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler.*;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.handler.*;
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
    public ConditionHandler buildConditionHandler() {
        return new JSqlParserConditionHandler(getStatement());
    }

    @Override
    public FromHandler buildFromHandler() {
        return new JSqlParserFromHandler(getStatement());
    }

    @Override
    public SelectItemHandler buildSelectItemHandler() {
        return new JSqlParserSelectItemHandler(getStatement());
    }

    @Override
    public SubSqlHandler buildSubSqlHandler() {
        return new JSqlParserSubSqlHandler((Select) getStatement());
    }

    @Override
    public FieldFromHandler buildFieldFromHandler() {
        return new JSqlParserSelectFieldFromHandler(getStatement());
    }


}
