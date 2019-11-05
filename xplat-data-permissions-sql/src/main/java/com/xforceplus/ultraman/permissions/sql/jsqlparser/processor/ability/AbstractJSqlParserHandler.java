package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

/**
 * 所有 handler 的基础抽像实现.
 * @version 0.1 2019/10/30 10:30
 * @auth dongbin
 * @since 1.8
 */
public abstract class AbstractJSqlParserHandler {

    private Statement statement;
    private PlainSelect plainSelect; // select 特殊的子类.
    private Class clazz;

    public AbstractJSqlParserHandler(Statement statement) {
        this(statement, Statement.class);
    }

    public AbstractJSqlParserHandler(Statement statement, Class clazz) {
        this.clazz = clazz;
        setStatement(statement);
    }

    /**
     * 专门处理 select 的 plainSelect.
     * @param plainSelect 目标 painSelect 实例.
     */
    public AbstractJSqlParserHandler(PlainSelect plainSelect) {
        this.plainSelect = plainSelect;
        this.clazz = Select.class;
    }

    public Statement getStatement() {
        return statement;
    }

    public Select getSelect() {
        return (Select) this.statement;
    }

    public Update getUpdate() {
        return (Update) this.statement;
    }

    public Delete getDelete() {
        return (Delete) this.statement;
    }

    public Insert getInsert() {
        return (Insert) this.statement;
    }

    public PlainSelect getSubSelect() {
        return plainSelect;
    }

    public boolean isSelect() {
        return plainSelect != null;
    }

    public boolean isUpdate() {
        return Update.class.isInstance(statement);
    }

    public boolean isDelete() {
        return Delete.class.isInstance(statement);
    }

    public boolean isInsert() {
        return Insert.class.isInstance(statement);
    }

    public boolean isSubSelect() {
        return plainSelect != null;
    }

    public final void setStatement(Statement statement) {
        if (!clazz.isInstance(statement)) {
            throw new IllegalArgumentException(
                String.format("Only %s statements can be processed.", clazz.getName()));
        }

        this.statement = statement;

        if (isSelect()) {
            plainSelect = (PlainSelect) ((Select) this.statement).getSelectBody();
        }
    }
}
