package com.xforceplus.ultraman.permissions.sql.define.values;

import com.xforceplus.ultraman.permissions.sql.define.ArithmeticSymbol;

import java.util.Objects;

/**
 * 表示值为一个运行.
 * @version 0.1 2019/10/29 19:03
 * @auth dongbin
 * @since 1.8
 */
public class ArithmeticValue implements Value<String> {

    private String value;

    public ArithmeticValue(String value) {
        if (value != null && !value.isEmpty()) {
            StringBuilder buff = new StringBuilder();
            for (char c : value.toCharArray()) {
                if (c == ' ') {
                    continue;
                }

                buff.append(c);
            }

            this.value = buff.toString();
        } else {
            throw new IllegalArgumentException("Invalid arithmetic expression.");
        }

    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean needQuotes() {
        return false;
    }

    @Override
    public String toSqlString() {
        StringBuilder buff = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (ArithmeticSymbol.getInstance(String.valueOf(c)) != null) {
                buff.append(" ").append(c).append(" ");
            } else {
                buff.append(c);
            }
        }
        return buff.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArithmeticValue)) return false;
        ArithmeticValue that = (ArithmeticValue) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return "ArithmeticValue{" +
            "value='" + value + '\'' +
            '}';
    }
}
