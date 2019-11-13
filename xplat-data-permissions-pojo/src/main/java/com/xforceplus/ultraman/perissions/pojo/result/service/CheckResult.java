package com.xforceplus.ultraman.perissions.pojo.result.service;

import com.xforceplus.ultraman.perissions.pojo.result.Result;

/**
 * 检查返回结果.
 * @version 0.1 2019/11/13 15:18
 * @auth dongbin
 * @since 1.8
 */
public class CheckResult extends Result<String> {

    public CheckResult(int code) {
        super(code);
    }

    public CheckResult(int code, String message, String value) {
        super(code, message, value);
    }
}
