package com.xforceplus.ultraman.permissions.sql.jsqlparser;

import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.ItemVisitor;
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

    private JSelectSqlProcessor selectSqlProcessor;
    private JUpdateSqlProcessor updateSqlProcessor;
    private JInsertSqlProcessor insertSqlProcessor;
    private JDeleteSqlProcessor deleteSqlProcessor;


    public JSql(Statement statement) {
        this.statement = statement;

        parserType();
    }

    @Override
    public SqlProcessor buildProcessor() {
        switch(type) {
            case SELECT: {
                if (selectSqlProcessor == null) {
                    selectSqlProcessor = new JSelectSqlProcessor(statement);
                }

                return selectSqlProcessor;
            }
            case UPDATE: {
                if (updateSqlProcessor == null) {
                    updateSqlProcessor = new JUpdateSqlProcessor(statement);
                }

                return updateSqlProcessor;
            }
            case INSERT:{
                if (insertSqlProcessor == null) {
                    insertSqlProcessor = new JInsertSqlProcessor(statement);
                }

                return insertSqlProcessor;
            }
            case DELETE:{
                if (deleteSqlProcessor == null) {
                    deleteSqlProcessor = new JDeleteSqlProcessor(statement);
                }

                return deleteSqlProcessor;
            }
            default:
                return UnableOperateSqlProcessor.getInstance();
        }
    }

    @Override
    public void visit(SqlProcessorVisitor visitor) {
        SqlProcessor processor = buildProcessor();
        switch(type) {
            case SELECT:
                visitor.visit((JSelectSqlProcessor)processor);
                break;
            case UPDATE:
                visitor.visit((JUpdateSqlProcessor)processor);
                break;
            case INSERT:
                visitor.visit((JInsertSqlProcessor)processor);
                break;
            case DELETE:
                visitor.visit((JDeleteSqlProcessor)processor);
                break;
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
    public boolean isSub() {
        return true;
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
