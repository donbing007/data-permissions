package com.xforceplus.ultraman.permissions.sql.define.values;

import com.xforceplus.ultraman.permissions.sql.define.ItemVisitor;

/**
 * 表示一个 JDBC 的参数占位符 ? 号.
 * @version 0.1 2019/11/19 09:50
 * @author dongbin
 * @since 1.8
 */
public class JdbcParameterValue implements Value<Character> {

    private static final Character PARAMETER_VALUE = '?';
    private static final JdbcParameterValue INSTANCE = new JdbcParameterValue();

    public static Value geInstance() {
        return INSTANCE;
    }

    private JdbcParameterValue() {
    }

    @Override
    public Character getValue() {
        return PARAMETER_VALUE;
    }

    @Override
    public boolean needQuotes() {
        return false;
    }

    @Override
    public String toSqlString() {
        return PARAMETER_VALUE.toString();
    }

    @Override
    public void visit(ItemVisitor visitor) {
        visitor.visit(this);
    }
}
