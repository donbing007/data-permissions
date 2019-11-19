package com.xforceplus.ultraman.permissions.sql.define.relationship;

import com.xforceplus.ultraman.permissions.sql.define.Item;

/**
 * @version 0.1 2019/11/12 11:11
 * @author dongbin
 * @since 1.8
 */
public abstract class Relationship implements Item {
    private Item left;
    private Item right;

    public Relationship(Item left, Item right) {
        this.left = left;
        this.right = right;
    }

    public Item getLeft() {
        return left;
    }

    public Item getRight() {
        return right;
    }

    protected abstract String getSymbol();

    @Override
    public String toSqlString() {
        StringBuilder buff = new StringBuilder();
        buff.append(left.toSqlString())
            .append(" ").append(getSymbol().trim()).append(" ")
            .append(right.toSqlString());
        return buff.toString();
    }
}
