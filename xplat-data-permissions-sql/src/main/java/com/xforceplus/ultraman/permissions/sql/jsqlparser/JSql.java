package com.xforceplus.ultraman.permissions.sql.jsqlparser;

import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessor;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.List;

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
        return null;
    }

    @Override
    public SqlType type() {
        return type;
    }

    //TODO: 还未实现子语句迭代.
    @Override
    public List<Sql> subSql() {
        List<Sql> subSqls = new ArrayList();
        statement.accept(new StatementVisitorAdapter() {
            @Override
            public void visit(Select select) {

            }

            @Override
            public void visit(Delete delete) {
                super.visit(delete);
            }

            @Override
            public void visit(Update update) {
                super.visit(update);
            }

            @Override
            public void visit(Insert insert) {
                super.visit(insert);
            }
        });

        return subSqls;
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
