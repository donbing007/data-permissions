package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor;

import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler.*;
import com.xforceplus.ultraman.permissions.sql.processor.SubSelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.handler.*;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * 了查询处理.
 * @version 0.1 2019/10/31 15:36
 * @auth dongbin
 * @since 1.8
 */
public class JSubSelectSqlProcessor implements SubSelectSqlProcessor {

    private PlainSelect plainSelect;

    public JSubSelectSqlProcessor(PlainSelect plainSelect) {
        this.plainSelect = plainSelect;
    }

    @Override
    public ConditionHandler buildConditionHandler() {
        return new JSqlParserConditionHandler(plainSelect);
    }

    @Override
    public FromHandler buildFromHandler() {
        return new JSqlParserFromHandler(plainSelect);
    }

    @Override
    public SelectItemHandler buildColumnHandler() {
        return new JSqlParserSelectItemHandler(plainSelect);
    }

    @Override
    public SubSqlHandler buildSubSqlHandler() {
        return new JSqlParserSubSqlHandler(plainSelect);
    }

    @Override
    public FieldFromHandler buildFieldFromHandler() {
        return new JSqlParserSelectFieldFromHandler(plainSelect);
    }

}
