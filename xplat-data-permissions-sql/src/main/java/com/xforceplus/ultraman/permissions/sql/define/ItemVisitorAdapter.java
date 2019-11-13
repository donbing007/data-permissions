package com.xforceplus.ultraman.permissions.sql.define;

import com.xforceplus.ultraman.permissions.sql.define.arithmetic.Arithmeitc;
import com.xforceplus.ultraman.permissions.sql.define.relationship.And;
import com.xforceplus.ultraman.permissions.sql.define.relationship.Or;
import com.xforceplus.ultraman.permissions.sql.define.values.*;

/**
 * 方便实现.
 * @version 0.1 2019/11/6 18:58
 * @auth dongbin
 * @since 1.8
 */
public class ItemVisitorAdapter implements ItemVisitor {

    @Override
    public void visit(DoubleValue item) {

    }

    @Override
    public void visit(LongValue item) {

    }

    @Override
    public void visit(NullValue item) {

    }

    @Override
    public void visit(StringValue item) {

    }

    @Override
    public void visit(UnknownValue item) {

    }

    @Override
    public void visit(Alias item) {

    }

    @Override
    public void visit(Condition item) {

    }

    @Override
    public void visit(Field item) {

    }

    @Override
    public void visit(From item) {

    }

    @Override
    public void visit(Func item) {

    }

    @Override
    public void visit(UpdateSet item) {

    }

    @Override
    public void visit(Parentheses item) {

    }

    @Override
    public void visit(Arithmeitc item) {

    }

    @Override
    public void visit(And and) {

    }

    @Override
    public void visit(Or or) {

    }
}
