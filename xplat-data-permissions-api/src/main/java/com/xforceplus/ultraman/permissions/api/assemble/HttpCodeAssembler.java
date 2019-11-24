package com.xforceplus.ultraman.permissions.api.assemble;

import com.xforceplus.ultraman.permissions.pojo.result.CheckStatus;
import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import org.springframework.http.HttpStatus;

/**
 * @version 0.1 2019/11/14 15:53
 * @author dongbin
 * @since 1.8
 */
public class HttpCodeAssembler {

    private HttpCodeAssembler() {}

    public static HttpStatus assemCheckStatus(CheckStatus status, HttpStatus success) {
        switch (status) {
            case ERROR: return HttpStatus.INTERNAL_SERVER_ERROR;
            default:
                return success;
        }
    }

    public static HttpStatus assemManagementStatus(ManagementStatus status, HttpStatus success) {
        switch(status) {
            case FAIL: return HttpStatus.INTERNAL_SERVER_ERROR;
            case LOSS: return HttpStatus.BAD_REQUEST;
            case REPETITION: return HttpStatus.BAD_REQUEST;
            case UNKNOWN: return HttpStatus.INTERNAL_SERVER_ERROR;
            default: return success;
        }
    }

}
