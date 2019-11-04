package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor;

import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler.JSqlParserConditionHandler;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler.JSqlParserFromHandler;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler.JSqlParserUpdateFieldFromHandler;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler.JSqlParserUpdateSetHandler;
import com.xforceplus.ultraman.permissions.sql.processor.UpdateSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.handler.ConditionHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.FieldFromHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.FromHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.UpdateSetHandler;
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
    public ConditionHandler buildConditionHandler() {
        return new JSqlParserConditionHandler(getStatement());
    }

    @Override
    public FromHandler buildFromHandler() {
        return new JSqlParserFromHandler(getStatement());
    }

    @Override
    public UpdateSetHandler buildUpdateSetHandler() {
        return new JSqlParserUpdateSetHandler(getStatement());
    }

    @Override
    public FieldFromHandler buildFieldFromHandler() {
        return new JSqlParserUpdateFieldFromHandler(getStatement());
    }

}
