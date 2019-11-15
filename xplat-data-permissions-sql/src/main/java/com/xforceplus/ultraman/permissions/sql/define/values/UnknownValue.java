package com.xforceplus.ultraman.permissions.sql.define.values;

import com.xforceplus.ultraman.permissions.sql.define.ItemVisitor;

import java.util.Objects;

/**
 * 表示一个无法识别的值类型.
 * @version 0.1 2019/10/29 15:54
 * @author dongbin
 * @since 1.8
 */
public class UnknownValue implements Value {

    private String value;

    private UnknownValue(String value) {
        this.value = value;
    }

    public static UnknownValue getInstance(String value) {
        return new UnknownValue(value);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean needQuotes() {
        return false;
    }

    @Override
    public String toSqlString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnknownValue)) return false;
        UnknownValue that = (UnknownValue) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return "UnknownValue{" +
            "value='" + value + '\'' +
            '}';
    }

    @Override
    public void visit(ItemVisitor visitor) {
        visitor.visit(this);
    }
}
