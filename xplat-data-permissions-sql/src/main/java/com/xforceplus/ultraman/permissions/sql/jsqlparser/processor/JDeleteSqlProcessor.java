package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor;

import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler.JSqlParserConditionHandler;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler.JSqlParserFromHandler;
import com.xforceplus.ultraman.permissions.sql.processor.DeleteSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.handler.ConditionHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.FromHandler;
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
    public ConditionHandler buildConditionHandler() {
        return new JSqlParserConditionHandler(getStatement());
    }

    @Override
    public FromHandler buildFromHandler() {
        return new JSqlParserFromHandler(getStatement());
    }
}
