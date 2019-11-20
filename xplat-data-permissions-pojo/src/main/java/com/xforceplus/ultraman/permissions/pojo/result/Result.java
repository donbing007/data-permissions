package com.xforceplus.ultraman.permissions.pojo.result;

import java.io.Serializable;
import java.util.Objects;

/**
 * service result 返回值.
 *
 * @version 0.1 2019/11/13 15:15
 * @author dongbin
 * @since 1.8
 */
public abstract class Result implements Serializable {

    private int code;
    private String message;

    public Result(int code) {
        this.code = code;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)  {
            return true;
        }
        if (!(o instanceof Result)) {
            return false;
        }
        Result result = (Result) o;
        return getCode() == result.getCode() &&
            Objects.equals(getMessage(), result.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getMessage());
    }

    @Override
    public String toString() {
        return "Result{" +
            "code=" + code +
            ", message='" + message + '\'' +
            '}';
    }
}
