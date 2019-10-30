package com.xforceplus.ultraman.permissions.sql.define;

import com.xforceplus.ultraman.permissions.sql.define.values.Value;

import java.util.List;
import java.util.Objects;

/**
 * 表示一个函数.
 * @version 0.1 2019/10/29 14:55
 * @auth dongbin
 * @since 1.8
 */
public class Func implements Item {
    private String name;
    private String alias;
    private List<Item> parameters;

    public Func(String name) {
        this(name, null, null);
    }

    public Func(String name, String alias) {
        this(name,null, alias);
    }

    public Func(String name, List<Item> parameters) {
        this(name,parameters, null);
    }

    public Func(String name, List<Item> parameters, String alias) {
        this.name = name;
        this.parameters = parameters;
        this.alias = alias;

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid function name.");
        }

        int index = this.name.indexOf('(');
        if (index > 0) {
            this.name = this.name.substring(0, index);
        }

        if (parameters != null && !parameters.isEmpty()) {
            parameters.stream().forEach(p -> {
                if (!Field.class.isInstance(p) && !Value.class.isInstance(p)) {
                    throw new IllegalArgumentException("Only Column and Value can be received!");
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
    public String toString() {
        return "Funcion{" +
            "name='" + name + '\'' +
            ", alias='" + alias + '\'' +
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
        if (alias != null) {
            buff.append(" AS ").append(alias);
        }
        return buff.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Func)) return false;
        Func func = (Func) o;
        return Objects.equals(getName(), func.getName()) &&
            Objects.equals(alias, func.alias) &&
            Objects.equals(getParameters(), func.getParameters());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), alias, getParameters());
    }

}
