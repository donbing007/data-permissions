package com.xforceplus.ultraman.permissions.sql.jsqlparser;

import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.JSubSelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitor;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.util.Objects;

/**
 * 表示一个子查询,嵌套查询和 union 除第一条语句之外.
 * @version 0.1 2019/10/31 15:34
 * @auth dongbin
 * @since 1.8
 */
public class JSubSql implements Sql {

    private PlainSelect subSelect;
    private JSubSelectSqlProcessor subSelectSqlProcessor;

    public JSubSql(PlainSelect subSelect) {
        this.subSelect = subSelect;
    }

    @Override
    public SqlProcessor buildProcessor() {
        if (subSelectSqlProcessor == null) {
            subSelectSqlProcessor = new JSubSelectSqlProcessor(subSelect);
        }
        return subSelectSqlProcessor;
    }

    @Override
    public void visit(SqlProcessorVisitor visitor) {
        SqlProcessor processor = buildProcessor();
        visitor.visit((JSubSelectSqlProcessor)processor);
    }

    @Override
    public SqlType type() {
        return SqlType.SELECT;
    }

    @Override
    public boolean isUnion() {
        // 子查询不会是联合查询.
        return false;
    }

    @Override
    public boolean isSub() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JSubSql)) return false;
        JSubSql jSubSql = (JSubSql) o;
        return Objects.equals(subSelect, jSubSql.subSelect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subSelect);
    }

    @Override
    public String toString() {
        return "JSubSql{" +
            "select=" + subSelect +
            '}';
    }

    @Override
    public String toSqlString() {
        return subSelect.toString();
    }
}
