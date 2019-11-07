package com.xforceplus.ultraman.permissions.sql.define;

import com.xforceplus.ultraman.permissions.sql.define.values.Value;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 表示一个条件.
 *
 * @version 0.1 2019/10/25 16:36
 * @auth dongbin
 * @since 1.8
 */
public class Condition implements Item{

    private Item column;
    private ConditionOperator operator;
    private List<Item> values;

    public Condition(Item column, ConditionOperator operator, Item value) {
        this(column, operator, Arrays.asList(value));
    }

    public Condition(Item column, ConditionOperator operator, List<Item> values) {
        this.column = column;
        this.operator = operator;
        this.values = values;

        switch (operator) {
            case BETWEEN: {
                if (values.size() != 2) {
                    throw new IllegalArgumentException("The symbol \"between\" must have two arguments.");
                }
                break;
            }
            case IN: {
                break;
            }
            default: {
                if (values.size() == 0) {
                    throw new IllegalArgumentException(
                        String.format("The symbol(%s) parameter must have an operation target.",
                            operator.getSymbol()));
                }
            }
        }
    }

    @Override
    public void visit(ItemVisitor visitor) {
        visitor.visit(this);
    }

    public boolean isFieldCondition() {
        return Field.class.isInstance(column);
    }

    public boolean isFuncCondition() {
        return Func.class.isInstance(column);
    }

    public <T> T getColumn() {
        return (T) column;
    }

    public ConditionOperator getOperator() {
        return operator;
    }

    public List getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Condition)) return false;
        Condition condition = (Condition) o;
        return Objects.equals(getColumn(), condition.getColumn()) &&
            getOperator() == condition.getOperator() &&
            Objects.equals(getValues(), condition.getValues());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColumn(), getOperator(), getValues());
    }

    @Override
    public String toString() {
        return "Condition{" +
            "column=" + column +
            ", operator=" + operator +
            ", values=" + values +
            '}';
    }

    @Override
    public String toSqlString() {
        StringBuilder buff = new StringBuilder();
        switch (operator) {
            case BETWEEN: {
                buff.append(buildLeft())
                    .append(" ").append(operator.getSymbol()).append(" ")
                    .append(values.get(0).toSqlString())
                    .append(" AND ")
                    .append(values.get(1).toSqlString());
                break;
            }
            case IN: {
                buff.append(buildLeft())
                    .append(" ").append(operator.getSymbol()).append(" ")
                    .append("(");
                for (int i = 0; i < values.size(); i++) {
                    if (i > 0) {
                        buff.append(",");
                    }
                    buff.append(values.get(i).toSqlString());
                }

                buff.append(")");
                break;
            }
            default: {
                buff.append(buildLeft())
                    .append(" ")
                    .append(operator.getSymbol())
                    .append(" ")
                    .append(values.get(0).toSqlString());
            }
        }

        return buff.toString();
    }


    private String buildLeft() {
        return column.toSqlString();
    }
}
