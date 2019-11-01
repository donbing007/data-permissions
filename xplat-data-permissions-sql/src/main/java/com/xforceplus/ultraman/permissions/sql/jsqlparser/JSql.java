package com.xforceplus.ultraman.permissions.sql.jsqlparser;

import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.JDeleteSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.JInsertSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.JSelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.JUpdateSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.DeleteSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitor;
import com.xforceplus.ultraman.permissions.sql.processor.UnableOperateSqlProcessor;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @version 0.1 2019/10/25 18:30
 * @auth dongbin
 * @since 1.8
 */
public class JSql implements Sql {

    private Statement statement;
    private SqlType type;

    public JSql(Statement statement) {
        this.statement = statement;

        parserType();
    }

    @Override
    public SqlProcessor buildProcessor() {
        switch(type) {
            case SELECT:
                return new JSelectSqlProcessor(statement);
            case UPDATE:
                return new JUpdateSqlProcessor(statement);
            case INSERT:
                return new JInsertSqlProcessor(statement);
            case DELETE:
                return new JDeleteSqlProcessor(statement);
            default:
                return UnableOperateSqlProcessor.getInstance();
        }
    }

    @Override
    public void visit(SqlProcessorVisitor visitor) {
        switch(type) {
            case SELECT:
                visitor.visit(new JSelectSqlProcessor(statement));
            case UPDATE:
                visitor.visit(new JUpdateSqlProcessor(statement));
            case INSERT:
                visitor.visit(new JInsertSqlProcessor(statement));
            case DELETE:
                visitor.visit(new JDeleteSqlProcessor(statement));
            default:
                visitor.visit(UnableOperateSqlProcessor.getInstance());
        }
    }

    @Override
    public SqlType type() {
        return type;
    }

    @Override
    public boolean isUnion() {

        if (SqlType.SELECT == type) {
            SelectBody body = ((Select) statement).getSelectBody();
            if (SetOperationList.class.isInstance(body)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JSql)) return false;
        JSql jSql = (JSql) o;
        return Objects.equals(statement, jSql.statement) &&
            type == jSql.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(statement, type);
    }

    @Override
    public String toString() {
        return "JSql{" +
            "statement=" + statement +
            ", type=" + type +
            '}';
    }


    @Override
    public String toSqlString() {
        return statement.toString();
    }

    private void parserType() {
        if (statement instanceof Select) {
            this.type = SqlType.SELECT;
        } else if (statement instanceof Update) {
            this.type = SqlType.UPDATE;
        } else if (statement instanceof Delete) {
            this.type = SqlType.DELETE;
        } else if (statement instanceof Insert) {
            this.type = SqlType.INSERT;
        } else {
            this.type = SqlType.UNKNOWN;
        }

    }
}
