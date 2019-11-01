package com.xforceplus.ultraman.permissions.sql.define;

import java.util.Objects;

/**
 * 当前 sql 语句操作的目标表来源.
 * @version 0.1 2019/10/25 17:35
 * @auth dongbin
 * @since 1.8
 */
public class From implements Item {
    /**
     * true 表示一个子查询,false 非子查询.
     */
    private boolean sub = false;
    /**
     * 表名,如果是子查询将为 null.
     */
    private String table;
    /**
     * 别名,如果没有为 null.
     */
    private Alias alias;

    public From(String table) {
        this(table, null, false);
    }

    public From(String table, Alias alias) {
        this(table, alias, false);
    }

    public From(String table, Alias alias, boolean sub) {
        this.sub = sub;
        this.table = table;
        this.alias = alias;
    }

    public boolean isSub() {
        return sub;
    }

    public String getTable() {
        return table;
    }

    public Alias getAlias() {
        return alias;
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
            ", alias='" + alias + '\'' +
            '}';
    }

    @Override
    public String toSqlString() {
        StringBuilder buff = new StringBuilder();
        buff.append(table);
        if (alias != null) {
            buff.append(alias.toSqlString());
        }
        return buff.toString();
    }
}
