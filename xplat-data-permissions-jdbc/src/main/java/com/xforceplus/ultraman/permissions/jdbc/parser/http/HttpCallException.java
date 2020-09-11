package com.xforceplus.ultraman.permissions.jdbc.parser.http;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): leo
 * 创建时间: 2020/9/10 7:05 PM
 */
public class HttpCallException extends RuntimeException {
    public HttpCallException(String message) {
        super(message);
    }

    public HttpCallException(String message, Throwable cause) {
        super(message, cause);
    }
}
