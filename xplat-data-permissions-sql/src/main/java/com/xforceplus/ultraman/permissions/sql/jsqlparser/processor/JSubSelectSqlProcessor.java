package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor;

import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability.*;
import com.xforceplus.ultraman.permissions.sql.processor.SubSelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.*;
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
    public ConditionAbility buildConditionAbility() {
        return new JSqlParserConditionAbility(plainSelect);
    }

    @Override
    public FromAbility buildFromAbility() {
        return new JSqlParserFromAbility(plainSelect);
    }

    @Override
    public SelectItemAbility buildSelectItemAbility() {
        return new JSqlParserSelectItemAbility(plainSelect);
    }

    @Override
    public SubSqlAbility buildSubSqlAbility() {
        return new JSqlParserSubSqlAbility(plainSelect);
    }

    @Override
    public FieldFromAbility buildFieldFromAbility() {
        return new JSqlParserSelectFieldFromAbility(plainSelect);
    }

}
