package com.xforceplus.ultraman.permissions.sql.define;

import java.util.Objects;

/**
 * 有别名.
 * @version 0.1 2019/11/7 11:16
 * @auth dongbin
 * @since 1.8
 */
public abstract class Aliasable {
    private Alias alias;

    public Aliasable(Alias alias) {
        this.alias = alias;
    }

    public Alias getAlias() {
        return alias;
    }

    public boolean hasAlias() {
        return alias != null;
    }

    @Override
    public String toString() {
        return "Aliasable{" +
            "alias=" + alias +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aliasable)) return false;
        Aliasable aliasable = (Aliasable) o;
        return Objects.equals(getAlias(), aliasable.getAlias());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAlias());
    }
}
