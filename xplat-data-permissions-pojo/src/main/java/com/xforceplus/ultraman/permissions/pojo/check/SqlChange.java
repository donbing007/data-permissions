package com.xforceplus.ultraman.permissions.pojo.check;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @version 0.1 2019/11/23 23:18
 * @author dongbin
 * @since 1.8
 */
public class SqlChange {

    private String newSql;
    private List<String> blackList;

    public SqlChange() {
    }

    public SqlChange(String newSql) {
        this(newSql, Collections.emptyList());
    }

    public SqlChange(String newSql, List<String> blackList) {
        this.newSql = newSql;
        this.blackList = blackList;
    }

    public String getNewSql() {
        return newSql;
    }

    public void setNewSql(String newSql) {
        this.newSql = newSql;
    }

    public List<String> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<String> blackList) {
        this.blackList = blackList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SqlChange)) return false;
        SqlChange sqlChange = (SqlChange) o;
        return Objects.equals(getNewSql(), sqlChange.getNewSql()) &&
            Objects.equals(getBlackList(), sqlChange.getBlackList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNewSql(), getBlackList());
    }

    @Override
    public String toString() {
        return "SqlChange{" +
            "newSql='" + newSql + '\'' +
            ", blackList=" + blackList +
            '}';
    }
}

