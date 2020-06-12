package com.xforceplus.ultraman.permissions.sql.hint;

import java.util.Objects;

/**
 * SQL 提示.
 *
 * @author dongbin
 * @version 0.1 2020/6/10 17:16
 * @since 1.8
 */
public class Hint {

    private boolean ignore;

    /**
     * 构造一个默认值的 hint.
     */
    public Hint() {
        ignore = false;
    }

    /**
     * 是否忽略权限校验.
     *
     * @return true 忽略,false 不忽略.默认不忽略.
     */
    public boolean isIgnore() {
        return this.ignore;
    }

    /**
     * 设置是否忽略.
     *
     * @param ignore true 忽略,false 不忽略.
     */
    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hint)) {
            return false;
        }
        Hint hint = (Hint) o;
        return isIgnore() == hint.isIgnore();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isIgnore());
    }

    @Override
    public String toString() {
        return "Hint{" +
            "ignore=" + ignore +
            '}';
    }
}
