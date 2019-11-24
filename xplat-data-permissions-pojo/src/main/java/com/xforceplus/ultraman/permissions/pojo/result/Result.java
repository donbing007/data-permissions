package com.xforceplus.ultraman.permissions.pojo.result;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

/**
 * service result 返回值.
 *
 * @param <C> 状态表示.
 * @param <V> 值类型.
 * @author dongbin
 * @version 0.1 2019/11/13 15:15
 * @since 1.8
 */
public abstract class Result<C, V> implements Serializable {

    private C status;
    private Collection<V> values = Collections.emptyList();
    private String message;

    public Result(C status) {
        this.status = status;
    }

    public Result(C status, String message) {
        this(status, Collections.emptyList(), message);
    }

    public Result(C status, V value) {
        this(status, value, null);
    }

    public Result(C status, V value, String message) {
        this(status, value != null ? Arrays.asList(value) : null, message);
    }

    public Result(C status, Collection<V> values, String message) {
        this.status = status;
        this.message = message;

        if (values != null) {
            this.values = new ArrayList<>(values);
        }
    }

    public C getStatus() {
        return status;
    }

    public void setStatus(C status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addValue(V value) {
        if (this.values == Collections.emptyList()) {
            this.values = Arrays.asList(value);
        } else {
            this.values.add(value);
        }
    }

    public Stream streamValues() {
        return values.stream();
    }

    public V findFirst() {
        Optional<V> v = values.stream().findFirst();
        if (v.isPresent()) {
            return v.get();
        } else {
            return null;
        }
    }

    public boolean hasValue() {
        return !values.isEmpty();
    }

    public Collection<V> getValues() {
        return values;
    }

    public void addValues(List<V> values) {
        for (V value : values) {
            addValue(value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Result)) {
            return false;
        }
        Result<?, ?> result = (Result<?, ?>) o;
        return Objects.equals(getStatus(), result.getStatus()) &&
            Objects.equals(values, result.values) &&
            Objects.equals(getMessage(), result.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), values, getMessage());
    }

    @Override
    public String toString() {
        return "Result{" +
            "status=" + status +
            ", values=" + values +
            ", message='" + message + '\'' +
            '}';
    }
}
