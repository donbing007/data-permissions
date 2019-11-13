package com.xforceplus.ultraman.perissions.pojo.result;

import java.io.Serializable;
import java.util.Objects;

/**
 * service result 返回值.
 * @version 0.1 2019/11/13 15:15
 * @auth dongbin
 * @since 1.8
 */
public abstract class Result<V> implements Serializable {

    private int code;
    private String message;
    private V value;

    public Result(int code) {
        this.code = code;
    }

    public Result(int code, String message, V value) {
        this.code = code;
        this.message = message;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result)) return false;
        Result<?> result = (Result<?>) o;
        return getCode() == result.getCode() &&
            Objects.equals(getMessage(), result.getMessage()) &&
            Objects.equals(getValue(), result.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getMessage(), getValue());
    }

    @Override
    public String toString() {
        return "Result{" +
            "code=" + code +
            ", message='" + message + '\'' +
            ", value=" + value +
            '}';
    }
}
