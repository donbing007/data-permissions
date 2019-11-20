package com.xforceplus.ultraman.permissions.api.assemble;

import com.xforceplus.ultraman.permissions.pojo.result.CheckStatus;
import org.springframework.http.HttpStatus;

/**
 * @version 0.1 2019/11/14 15:53
 * @author dongbin
 * @since 1.8
 */
public class HttpCodeAssembler {

    private HttpCodeAssembler() {}

    public static HttpStatus assem(CheckStatus status, HttpStatus successStatus) {
        switch (status) {
            case ERROR: return HttpStatus.INTERNAL_SERVER_ERROR;
            default:
                return successStatus;
        }
    }

}
