package com.xforceplus.ultraman.permissions.pojo.page;

import java.util.Objects;

/**
 * 继续信息.
 * @author dongbin
 * @version 0.1 2019/11/21 12:32
 * @since 1.8
 */
public class Continuation {

    private static final int DEFALUT_SIZE = 10;

    public static final Continuation DEFALUT = new Continuation(0L, DEFALUT_SIZE);

    private Long start;
    private int size;

    public Continuation() {
        this(-1L, DEFALUT_SIZE);
    }

    public Continuation(Long start, int size) {
        this.start = start;

        if (size <= 0) {
            this.size = DEFALUT_SIZE;
        } else {
            this.size = size;
        }
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Continuation)) return false;
        Continuation aContinue = (Continuation) o;
        return getSize() == aContinue.getSize() &&
            Objects.equals(getStart(), aContinue.getStart());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStart(), getSize());
    }

    @Override
    public String toString() {
        return "Continue{" +
            "start=" + start +
            ", size=" + size +
            '}';
    }
}
