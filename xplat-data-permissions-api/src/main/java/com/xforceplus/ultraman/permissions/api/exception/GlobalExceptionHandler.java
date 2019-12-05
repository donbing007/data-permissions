package com.xforceplus.ultraman.permissions.api.exception;

import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理.
 * @author dongbin
 * @version 0.1 2019/12/5 11:43
 * @since 1.8
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public Result handleException(HttpServletResponse response, Throwable e) throws Exception {
        logger.error(e.getMessage(), e);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new Result(ManagementStatus.FAIL, e.getMessage()){};
    }
}
