package com.xforceplus.ultraman.permissions.pojo.result.service;

import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.Result;
import com.xforceplus.ultraman.permissions.pojo.rule.DataRule;
import com.xforceplus.ultraman.permissions.pojo.rule.DataRuleV2;

import java.util.Collection;

/**
 * @author dongbin
 * @version 0.1 2019/11/23 23:12
 * @since 1.8
 */
public class DataRuleManagementResultV2 extends Result<ManagementStatus, DataRuleV2> {
    public DataRuleManagementResultV2(ManagementStatus status) {
        super(status);
    }

    public DataRuleManagementResultV2(ManagementStatus status, String message) {
        super(status, message);
    }

    public DataRuleManagementResultV2(ManagementStatus status, DataRuleV2 value) {
        super(status, value);
    }

    public DataRuleManagementResultV2(ManagementStatus status, DataRuleV2 value, String message) {
        super(status, value, message);
    }

    public DataRuleManagementResultV2(ManagementStatus status, Collection<DataRuleV2> values, String message) {
        super(status, values, message);
    }
}
