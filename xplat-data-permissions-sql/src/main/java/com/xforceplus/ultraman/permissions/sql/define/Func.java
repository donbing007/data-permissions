package com.xforceplus.ultraman.permissions.sql.define;

import com.xforceplus.ultraman.permissions.sql.define.values.Value;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 表示一个函数.
 * @version 0.1 2019/10/29 14:55
 * @auth dongbin
 * @since 1.8
 */
public class Func extends Aliasable implements Item {
    private String name;
    private List<Item> parameters;

    public Func(String name) {
        this(name, null, null);
    }

    public Func(String name, Alias alias) {
        this(name,null, alias);
    }

    public Func(String name, List<Item> parameters) {
        this(name,parameters, null);
    }

    public Func(String name, List<Item> parameters, Alias alias) {
        super(alias);
        this.name = name;
        this.parameters = parameters != null ? parameters : Collections.emptyList();

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid function name.");
        }

        int index = this.name.indexOf('(');
        if (index > 0) {
            this.name = this.name.substring(0, index);
        }

        if (parameters != null && !parameters.isEmpty()) {
            parameters.stream().forEach(p -> {
                if (!Field.class.isInstance(p) && !Value.class.isInstance(p) && !Func.class.isInstance(p)) {
                    throw new IllegalArgumentException("Only Column, Value and Function can be received!");
                }
            });
        }
    }

    public String getName() {
        return name;
    }

    public List<Item> getParameters() {
        return parameters;
    }

    public boolean hasParameter() {
        return this.parameters != null && !this.parameters.isEmpty();
    }

    @Override
    public void visit(ItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Funcion{" +
            "name='" + name + '\'' +
            ", alias='" + (hasAlias() ? getAlias() : "null") + '\'' +
            ", parameters=" + parameters +
            '}';
    }

    @Override
    public String toSqlString() {
        StringBuilder buff = new StringBuilder();
        buff.append(name).append("(");

        if (hasParameter()) {
            for (int i = 0; i < parameters.size(); i++) {
                if (i > 0) {
                    buff.append(",");
                }
                buff.append(parameters.get(i).toSqlString());
            }
        }

        buff.append(")");
        if (hasAlias()) {
            buff.append(getAlias().toSqlString());
        }
        return buff.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Func)) return false;
        Func func = (Func) o;
        return Objects.equals(getName(), func.getName()) &&
            Objects.equals(getParameters(), func.getParameters()) &&
            Objects.equals(getAlias(), func.getAlias());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getParameters(), getAlias());
    }
}
