package com.xforceplus.ultraman.permissions.pojo.result.service;

import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.Result;
import com.xforceplus.ultraman.permissions.pojo.rule.DataRuleV2;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;

import java.util.Collection;

/**
 * @author dongbin
 * @version 0.1 2019/11/21 15:54
 * @since 1.8
 */
public class FieldRuleManagementResultV2 extends Result<ManagementStatus, DataRuleV2> {

    public FieldRuleManagementResultV2(ManagementStatus status) {
        super(status);
    }

    public FieldRuleManagementResultV2(ManagementStatus status, String message) {
        super(status, message);
    }

    public FieldRuleManagementResultV2(ManagementStatus status, DataRuleV2 value) {
        super(status, value);
    }

    public FieldRuleManagementResultV2(ManagementStatus status, DataRuleV2 value, String message) {
        super(status, value, message);
    }

    public FieldRuleManagementResultV2(ManagementStatus status, Collection<DataRuleV2> values, String message) {
        super(status, values, message);
    }
}
