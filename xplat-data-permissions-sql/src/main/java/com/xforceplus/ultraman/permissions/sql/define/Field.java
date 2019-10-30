package com.xforceplus.ultraman.permissions.sql.define;

import java.util.Objects;

/**
 * 表示查询字段.
 *
 * @version 0.1 2019/10/28 18:12
 * @auth dongbin
 * @since 1.8
 */
public class Field implements Item {

    private static final Field ALL_ITEM = new Field("*");

    private String table;
    private String name;
    private String alias;

    public static Field getAllField() {
        return ALL_ITEM;
    }

    public Field(String name) {
        this(null, name, null);
    }

    public Field(String name, String alias) {
        this(null, name, alias);
    }

    public Field(String table, String name, String alias) {
        this.table = table;
        this.name = name;
        this.alias = alias;

        if (this.name == null) {
            throw new IllegalArgumentException("Invalid field name!");
        }
    }


    public boolean isAllColumns() {
        return "*".equals(name);
    }

    public String getTable() {
        return table;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public boolean isAll() {
        return "*".equals(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Field)) return false;
        Field field = (Field) o;
        return Objects.equals(getTable(), field.getTable()) &&
            getName().equals(field.getName()) &&
            Objects.equals(getAlias(), field.getAlias());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTable(), getName(), getAlias());
    }

    @Override
    public String toString() {
        return "Field{" +
            "table='" + table + '\'' +
            ", name='" + name + '\'' +
            ", alias='" + alias + '\'' +
            '}';
    }

    @Override
    public String toSqlString() {
        StringBuilder buff = new StringBuilder();
        if (table != null && !table.isEmpty()) {
            buff.append(table).append(".");
        }
        buff.append(name);
        if (alias != null && !alias.isEmpty()) {
            buff.append(" AS ").append(alias);
        }

        return buff.toString();
    }
}
