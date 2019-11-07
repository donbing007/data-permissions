package com.xforceplus.ultraman.permissions.sql.define;

/**
 * 表示一个括号.
 * @version 0.1 2019/11/7 14:30
 * @auth dongbin
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
}
