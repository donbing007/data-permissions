package com.xforceplus.ultraman.permissions.sql.define;

import java.util.Objects;

/**
 * 表示一个括号.
 *
 * @author dongbin
 * @version 0.1 2019/11/7 14:30
 * @since 1.8
 */
public class Parentheses extends Aliasable implements Item {

    private Item item;

    public Parentheses(Item item) {
        this(item, null);
    }

    public Parentheses(Item item, Alias alias) {
        super(alias);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public String toSqlString() {
        StringBuilder buff = new StringBuilder();
        buff.append("(")
            .append(item.toSqlString())
            .append(")");

        if (hasAlias()) {
            buff.append(getAlias().toSqlString());
        }
        return buff.toString();
    }

    @Override
    public void visit(ItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parentheses)) return false;
        if (!super.equals(o)) return false;
        Parentheses that = (Parentheses) o;
        return Objects.equals(getItem(), that.getItem()) &&
            Objects.equals(getAlias(), that.getAlias());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getItem(), getAlias());
    }

    @Override
    public String toString() {
        return "Parentheses{" +
            "item=" + item +
            ", alias='" + (hasAlias() ? getAlias() : "null") + '\'' +
            '}';
    }
}
