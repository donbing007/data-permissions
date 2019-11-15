package com.xforceplus.ultraman.permissions.sql.processor;


/**
 * 表壳 processor 异常.
 *
 * @author dongbin
 * @version 0.1 2019/11/7 19:06
 * @since 1.8
 */
public class ProcessorException extends RuntimeException {

    public ProcessorException() {
    }

    public ProcessorException(String message) {
        super(message);
    }

    public ProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

}
