package com.xforceplus.ultraman.permissions.pojo.result.service;

import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.Result;
import com.xforceplus.ultraman.permissions.pojo.rule.DataRule;

import java.util.Collection;

/**
 * @author dongbin
 * @version 0.1 2019/11/23 23:12
 * @since 1.8
 */
public class DataRuleManagementResult extends Result<ManagementStatus, DataRule> {
    public DataRuleManagementResult(ManagementStatus status) {
        super(status);
    }

    public DataRuleManagementResult(ManagementStatus status, String message) {
        super(status, message);
    }

    public DataRuleManagementResult(ManagementStatus status, DataRule value) {
        super(status, value);
    }

    public DataRuleManagementResult(ManagementStatus status, DataRule value, String message) {
        super(status, value, message);
    }

    public DataRuleManagementResult(ManagementStatus status, Collection<DataRule> values, String message) {
        super(status, values, message);
    }
}
