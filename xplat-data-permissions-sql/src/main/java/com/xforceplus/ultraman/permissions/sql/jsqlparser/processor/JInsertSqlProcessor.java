package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor;

import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler.JSqlParserFromHandler;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler.JSqlParserInsertItemHandler;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler.JSqlParserInsertValueHandler;
import com.xforceplus.ultraman.permissions.sql.processor.InsertSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.handler.InsertValueHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.SelectItemHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.FromHandler;
import com.xforceplus.ultraman.permissions.sql.processor.handler.InsertItemHandler;
import net.sf.jsqlparser.statement.Statement;

/**
 * insert
 * @version 0.1 2019/10/25 18:55
 * @auth dongbin
 * @since 1.8
 */
public class JInsertSqlProcessor extends JSqlProcessor implements InsertSqlProcessor {
    public JInsertSqlProcessor(Statement statement) {
        super(statement);
    }

    @Override
    public FromHandler buildFromHandler() {
        return new JSqlParserFromHandler(getStatement());
    }

    @Override
    public InsertValueHandler buildInsertValueHandler() {
        return new JSqlParserInsertValueHandler(getStatement());
    }

    @Override
    public InsertItemHandler buildInsertItemHandler() {
        return new JSqlParserInsertItemHandler(getStatement());
    }


}
