package com.xforceplus.ultraman.permissions.sql.jsqlparser;

import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.JSubSelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitor;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;

import java.util.Objects;

/**
 * 表示一个子查询,嵌套查询和 union 除第一条语句之外.
 *
 * @version 0.1 2019/10/31 15:34
 * @author dongbin
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
        visitor.visit((JSubSelectSqlProcessor) processor);
    }

    @Override
    public SqlType type() {
        return SqlType.SELECT;
    }

    @Override
    public boolean isUnion() {

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

    public static void main(String[] args) throws Exception {
        String sql = "select * from t1 where c1=? and c2=?";
        Statement statement = CCJSqlParserUtil.parse(sql);
        System.out.println(statement);
    }
}
