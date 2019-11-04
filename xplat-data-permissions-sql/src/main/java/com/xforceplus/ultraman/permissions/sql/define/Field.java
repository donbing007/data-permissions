package com.xforceplus.ultraman.permissions.sql.define;

import java.util.Objects;

/**
 * 表示查询字段.
 *
 * @version 0.1 2019/10/28 18:12
 * @auth dongbin
 * @since 1.8
 */
public class Field implements Item  {

    private static final Field ALL_ITEM = new Field("*");

    private String ref;
    private String name;
    private Alias alias;

    public static Field getAllField() {
        return ALL_ITEM;
    }

    public Field(String name) {
        this(null, name, null);
    }

    public Field(String ref, String name) {
        this(ref, name, null);
    }

    public Field(String name, Alias alias) {
        this(null, name, alias);
    }

    public Field(String ref, String name, Alias alias) {
        this.ref = ref;
        this.name = name;
        this.alias = alias;

        if (this.name == null) {
            throw new IllegalArgumentException("Invalid field name!");
        }
    }


    public boolean isAllColumns() {
        return "*".equals(name);
    }

    public String getRef() {
        return ref;
    }

    public String getName() {
        return name;
    }

    public Alias getAlias() {
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
        return Objects.equals(getRef(), field.getRef()) &&
            Objects.equals(getName(), field.getName()) &&
            Objects.equals(getAlias(), field.getAlias());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRef(), getName(), getAlias());
    }

    @Override
    public String toString() {
        return "Field{" +
            "ref='" + ref + '\'' +
            ", name='" + name + '\'' +
            ", alias=" + alias +
            '}';
    }

    @Override
    public String toSqlString() {
        StringBuilder buff = new StringBuilder();
        if (ref != null && !ref.isEmpty()) {
            buff.append(ref).append(".");
        }
        buff.append(name);
        if (alias != null) {
            buff.append(alias.toSqlString());
        }

        return buff.toString();
    }
}
