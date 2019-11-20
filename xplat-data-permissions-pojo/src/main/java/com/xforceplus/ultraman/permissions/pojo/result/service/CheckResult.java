package com.xforceplus.ultraman.permissions.pojo.result.service;

import com.xforceplus.ultraman.permissions.pojo.result.Result;

import java.util.List;

/**
 * 检查返回结果.
 * @version 0.1 2019/11/13 15:18
 * @author dongbin
 * @since 1.8
 */
public class CheckResult extends Result {

    private String newSql;
    private List<String> backList;

    public CheckResult(int code) {
        super(code);
    }

    public CheckResult(int code, String message) {
        super(code, message);
    }

    public String getNewSql() {
        return newSql;
    }

    public void setNewSql(String newSql) {
        this.newSql = newSql;
    }

    public List<String> getBackList() {
        return backList;
    }

    public void setBackList(List<String> backList) {
        this.backList = backList;
    }
}
