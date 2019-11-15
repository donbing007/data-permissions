package com.xforceplus.ultraman.permissions.sql.define;

import com.xforceplus.ultraman.permissions.sql.define.values.Value;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ValueHelper;

import java.util.Objects;

/**
 * update set.
 *
 * @author dongbin
 * @version 0.1 2019/10/30 10:22
 * @since 1.8
 */
public class UpdateSet implements Item {

    private Field field;
    private Item value;

    public UpdateSet(Field field, Value value) {
        this.field = field;
        this.value = value;
    }

    public UpdateSet(Field field, Func function) {
        this.field = field;
        this.value = function;
    }

    public Field getField() {
        return field;
    }

    public <T> T getValue() {
        return (T) value;
    }

    public boolean isValueSet() {
        return Value.class.isInstance(value);
    }

    public boolean isFuncSet() {
        return Func.class.isInstance(value);
    }

    @Override
    public String toSqlString() {
        StringBuilder buff = new StringBuilder();
        buff.append(field.toSqlString());
        buff.append(" = ");
        buff.append(value.toSqlString());

        return buff.toString();
    }

    @Override
    public void visit(ItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateSet)) return false;
        UpdateSet updateSet = (UpdateSet) o;
        return Objects.equals(getField(), updateSet.getField()) &&
            Objects.equals(getValue(), updateSet.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getField(), getValue());
    }

    @Override
    public String toString() {
        return "UpdateSet{" +
            "field=" + field +
            ", value=" + value +
            '}';
    }
}
