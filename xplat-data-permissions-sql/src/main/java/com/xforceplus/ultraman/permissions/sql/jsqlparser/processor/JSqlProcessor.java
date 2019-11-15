package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor;

import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessor;
import net.sf.jsqlparser.statement.Statement;

/**
 * jsqlparser 的 processor 超类.
 * @version 0.1 2019/10/25 18:49
 * @author dongbin
 * @since 1.8
 */
public abstract class JSqlProcessor implements SqlProcessor {

    private Statement statement;

    public JSqlProcessor(Statement statement) {
        this.statement = statement;
    }

    public Statement getStatement() {
        return statement;
    }
}
