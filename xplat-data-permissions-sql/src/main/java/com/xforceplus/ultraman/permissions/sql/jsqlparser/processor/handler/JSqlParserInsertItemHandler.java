package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ConversionHelper;
import com.xforceplus.ultraman.permissions.sql.processor.handler.InsertItemHandler;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * insert column 子句.
 * @version 0.1 2019/10/30 15:58
 * @auth dongbin
 * @since 1.8
 */
public class JSqlParserInsertItemHandler extends AbstractJSqlParserHandler implements InsertItemHandler {

    public JSqlParserInsertItemHandler(Statement statement) {
        super(statement, Insert.class);
    }

    @Override
    public List<Field> list() {
        Insert insert = getInsert();
        List<Column> columns = insert.getColumns();
        if (columns != null && !columns.isEmpty()) {
            return columns.stream().map(c -> ConversionHelper.convert(c)).collect(Collectors.toList());
        } else {
            return Collections.EMPTY_LIST;
        }
    }

}
