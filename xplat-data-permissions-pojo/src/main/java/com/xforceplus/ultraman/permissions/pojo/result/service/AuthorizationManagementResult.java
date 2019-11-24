package com.xforceplus.ultraman.permissions.pojo.result.service;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.Result;

import java.util.Collection;

/**
 * @version 0.1 2019/11/24 23:22
 * @author dongbin
 * @since 1.8
 */
public class AuthorizationManagementResult extends Result<ManagementStatus, Authorization> {
    public AuthorizationManagementResult(ManagementStatus status) {
        super(status);
    }

    public AuthorizationManagementResult(ManagementStatus status, String message) {
        super(status, message);
    }

    public AuthorizationManagementResult(ManagementStatus status, Authorization value) {
        super(status, value);
    }

    public AuthorizationManagementResult(ManagementStatus status, Authorization value, String message) {
        super(status, value, message);
    }

    public AuthorizationManagementResult(ManagementStatus status, Collection<Authorization> values, String message) {
        super(status, values, message);
    }
}
