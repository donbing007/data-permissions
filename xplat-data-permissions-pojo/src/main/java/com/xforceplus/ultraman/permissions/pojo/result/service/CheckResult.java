package com.xforceplus.ultraman.permissions.pojo.result.service;

import com.xforceplus.ultraman.permissions.pojo.check.SqlChange;
import com.xforceplus.ultraman.permissions.pojo.result.CheckStatus;
import com.xforceplus.ultraman.permissions.pojo.result.Result;

import java.util.List;

/**
 * 检查返回结果.
 * @version 0.1 2019/11/13 15:18
 * @author dongbin
 * @since 1.8
 */
public class CheckResult extends Result<CheckStatus, SqlChange> {

    public CheckResult(CheckStatus status) {
        super(status);
    }

    public CheckResult(CheckStatus status, String message) {
        super(status, message);
    }

    public CheckResult(CheckStatus status, SqlChange value) {
        super(status, value);
    }

    public CheckResult(CheckStatus status, SqlChange value, String message) {
        super(status, value, message);
    }

    public CheckResult(CheckStatus status, List<SqlChange> values, String message) {
        super(status, values, message);
    }
}
