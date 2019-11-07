package com.xforceplus.ultraman.permissions.sql.define.arithmetic;

import com.xforceplus.ultraman.permissions.sql.define.Alias;
import com.xforceplus.ultraman.permissions.sql.define.Aliasable;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.ItemVisitor;

import java.util.Objects;

/**
 * 表示一个左值和右值的算术.
 * @version 0.1 2019/11/7 09:52
 * @auth dongbin
 * @since 1.8
 */
public class Arithmeitc extends Aliasable implements Item {

    private Item left; // Field, func, value or self
    private Item right; // Field, func, value or self
    private ArithmeticSymbol symbol;

    public Arithmeitc(Item left, Item right, ArithmeticSymbol symbol) {
        this(left, right, symbol, null);
    }

    public Arithmeitc(Item left, Item right, ArithmeticSymbol symbol, Alias alias) {
        super(alias);
        this.left = left;
        this.right = right;
        this.symbol = symbol;
    }

    public Item getLeft() {
        return left;
    }

    public Item getRight() {
        return right;
    }

    public ArithmeticSymbol getSymbol() {
        return symbol;
    }

    @Override
    public String toSqlString() {
        StringBuilder buff = new StringBuilder();
        buff.append(left.toSqlString())
            .append(" ")
            .append(symbol.getSymbol())
            .append(" ")
            .append(right.toSqlString());

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
        if (!(o instanceof Arithmeitc)) return false;
        if (!super.equals(o)) return false;
        Arithmeitc that = (Arithmeitc) o;
        return Objects.equals(getLeft(), that.getLeft()) &&
            Objects.equals(getRight(), that.getRight()) &&
            Objects.equals(getAlias(), that.getAlias()) &&
            getSymbol() == that.getSymbol();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLeft(), getRight(), getSymbol());
    }

    @Override
    public String toString() {
        return "Arithmeitc{" +
            "left=" + left +
            ", right=" + right +
            ", symbol=" + symbol +
            ", alias='" + (hasAlias() ? getAlias() : "null") + '\'' +
            '}';
    }
}
