package com.xforceplus.ultraman.permissions.sql.define.values;

import java.util.Objects;

/**
 * @version 0.1 2019/10/29 15:23
 * @auth dongbin
 * @since 1.8
 */
public class StringValue implements Value<String> {
    private String value = "";

    public StringValue(String value) {
        if (value == null) {
            throw new NullPointerException("Value cannot be null.");
        }
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public boolean needQuotes() {
        return true;
    }

    @Override
    public String toSqlString() {
        return "'" + value + "'";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringValue)) return false;
        StringValue that = (StringValue) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }


}
