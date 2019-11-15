package com.xforceplus.ultraman.permissions.sql.define;

import java.util.Objects;

/**
 * 当前 sql 语句操作的目标表来源.
 *
 * @author dongbin
 * @version 0.1 2019/10/25 17:35
 * @since 1.8
 */
public class From extends Aliasable implements Item {
    /**
     * true 表示一个子查询,false 非子查询.
     */
    private boolean sub = false;
    /**
     * 表名,如果是子查询将为 null.
     */
    private String table;

    public From(String table) {
        this(table, null, false);
    }

    public From(String table, Alias alias) {
        this(table, alias, false);
    }

    public From(String table, Alias alias, boolean sub) {
        super(alias);
        this.sub = sub;
        this.table = table;
    }

    public boolean isSub() {
        return sub;
    }

    public String getTable() {
        return table;
    }

    @Override
    public void visit(ItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof From)) return false;
        From from = (From) o;
        return isSub() == from.isSub() &&
            Objects.equals(getTable(), from.getTable()) &&
            Objects.equals(getAlias(), from.getAlias());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isSub(), getTable(), getAlias());
    }

    @Override
    public String toString() {
        return "From{" +
            "sub=" + sub +
            ", table='" + table + '\'' +
            ", alias='" + (hasAlias() ? getAlias() : "null") + '\'' +
            '}';
    }

    @Override
    public String toSqlString() {
        StringBuilder buff = new StringBuilder();
        buff.append(table);
        if (hasAlias()) {
            buff.append(getAlias().toSqlString());
        }
        return buff.toString();
    }
}
