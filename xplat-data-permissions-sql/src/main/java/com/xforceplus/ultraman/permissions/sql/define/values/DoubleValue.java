package com.xforceplus.ultraman.permissions.sql.define.values;

import com.xforceplus.ultraman.permissions.sql.define.ItemVisitor;

import java.util.Objects;

/**
 * @version 0.1 2019/10/29 15:26
 * @auth dongbin
 * @since 1.8
 */
public class DoubleValue implements Value<Double> {

    private double value;

    public DoubleValue(double value) {
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public boolean needQuotes() {
        return false;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public String toSqlString() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleValue)) return false;
        DoubleValue that = (DoubleValue) o;
        return Double.compare(that.getValue(), getValue()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public void visit(ItemVisitor visitor) {
        visitor.visit(this);
    }
}
