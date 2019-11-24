package com.xforceplus.ultraman.permissions.pojo.result.service;

import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.Result;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;

import java.util.Collection;

/**
 * @author dongbin
 * @version 0.1 2019/11/21 15:54
 * @since 1.8
 */
public class FieldRuleManagementResult extends Result<ManagementStatus, FieldRule> {


    public FieldRuleManagementResult(ManagementStatus status) {
        super(status);
    }

    public FieldRuleManagementResult(ManagementStatus status, String message) {
        super(status, message);
    }

    public FieldRuleManagementResult(ManagementStatus status, FieldRule value) {
        super(status, value);
    }

    public FieldRuleManagementResult(ManagementStatus status, FieldRule value, String message) {
        super(status, value, message);
    }

    public FieldRuleManagementResult(ManagementStatus status, Collection<FieldRule> values, String message) {
        super(status, values, message);
    }
}
