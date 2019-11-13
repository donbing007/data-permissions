package com.xforceplus.ultraman.permissions.sql.define.relationship;

import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.ItemVisitor;

/**
 * @version 0.1 2019/11/12 11:07
 * @auth dongbin
 * @since 1.8
 */
public class Or extends Relationship {


    public Or(Item left, Item right) {
        super(left, right);
    }

    @Override
    public void visit(ItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    protected String getSymbol() {
        return "OR";
    }
}
