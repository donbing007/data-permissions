package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor;

import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler.JSqlParserConditionHandler;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler.JSqlParserFromHandler;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler.JSqlParserSelectItemHandler;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.handler.SelectItemHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.ConditionHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.FromHandler;
import net.sf.jsqlparser.statement.Statement;

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
    public SelectItemHandler buildColumnHandler() {
        return new JSqlParserSelectItemHandler(getStatement());
    }
}
