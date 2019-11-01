package com.xforceplus.ultraman.permissions.sql.define;

import java.util.Objects;

/**
 * @version 0.1 2019/10/31 17:45
 * @auth dongbin
 * @since 1.8
 */
public class Alias implements Item {

    private String name;
    private boolean useAs;

    public Alias(String name) {
        this(name,false);
    }

    public Alias(String name, boolean useAs) {
        this.name = name;
        this.useAs = useAs;
    }

    @Override
    public String toSqlString() {
        StringBuilder buff = new StringBuilder();
        if (useAs) {
            buff.append(" AS ");
        }
        buff.append(name);

        return buff.toString();
    }

    public String getName() {
        return name;
    }

    public boolean isUseAs() {
        return useAs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alias)) return false;
        Alias alias = (Alias) o;
        return isUseAs() == alias.isUseAs() &&
            Objects.equals(getName(), alias.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), isUseAs());
    }

    @Override
    public String toString() {
        return "Alias{" +
            "name='" + name + '\'' +
            ", useAs=" + useAs +
            '}';
    }
}
