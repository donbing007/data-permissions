package com.xforceplus.ultraman.permissions.sql.define.values;

import java.util.Objects;

/**
 * 整形.
 * @version 0.1 2019/10/29 15:17
 * @auth dongbin
 * @since 1.8
 */
public class LongValue implements Value<Long> {

    private long value;

    public LongValue(long value) {
        this.value = value;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    @Override
    public boolean needQuotes() {
        return false;
    }

    @Override
    public String toSqlString() {
        return Long.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongValue)) return false;
        LongValue longValue = (LongValue) o;
        return getValue() == longValue.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}