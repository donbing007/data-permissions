package com.xforceplus.ultraman.permissions.sql.define;

import com.xforceplus.ultraman.permissions.sql.define.arithmetic.Arithmeitc;
import com.xforceplus.ultraman.permissions.sql.define.relationship.And;
import com.xforceplus.ultraman.permissions.sql.define.relationship.Or;
import com.xforceplus.ultraman.permissions.sql.define.values.*;

/**
 * 元素访问定义.
 *
 * @author dongbin
 * @version 0.1 2019/11/6 18:53
 * @since 1.8
 */
public interface ItemVisitor {

    void visit(DoubleValue item);

    void visit(LongValue item);

    void visit(NullValue item);

    void visit(StringValue item);

    void visit(UnknownValue item);

    void visit(Alias item);

    void visit(Condition item);

    void visit(Field item);

    void visit(From item);

    void visit(Func item);

    void visit(UpdateSet item);

    void visit(Parentheses item);

    void visit(Arithmeitc item);

    void visit(And and);

    void visit(Or or);
}
