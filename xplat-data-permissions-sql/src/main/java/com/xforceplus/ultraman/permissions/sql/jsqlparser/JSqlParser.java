package com.xforceplus.ultraman.permissions.sql.jsqlparser;

import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.SqlParser;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

/**
 * 基于 jsqlparser 的实现.
 *
 * https://github.com/JSQLParser/JSqlParser
 *
 * @version 0.1 2019/10/24 10:50
 * @auth dongbin
 * @since 1.8
 */
public class JSqlParser implements SqlParser {

    private static final SqlParser SQL_PARSER = new JSqlParser();

    private JSqlParser() {

    }

    public static SqlParser getInstance() {
        return SQL_PARSER;
    }

    @Override
    public Sql parser(String sql) throws ProcessorException {
        Statement statment;
        try {
            statment = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            throw new ProcessorException(e.getMessage(), e);
        }

        return new JSql(statment);
    }

    @Override
    public boolean isSupport(String sql) {
        try {
            CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            return false;
        }

        return true;
    }
}
