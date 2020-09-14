package com.xforceplus.ultraman.permissions.jdbc.parser.http.response;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * @param <T> the body type
 * 作者(@author): liwei
 * 创建时间: 2020/5/21 4:36 PM
 */
public class HttpResponse<T> {

    private final int code;
    private final T result;
    private String message;

    /**
     *
     * @param code
     * @param message
     */
    public HttpResponse(int code, String message, T result) {
        this.code = code;
        this.result = result;
        this.message = message;
    }

    public int getStatusCode() {
        return code;
    }

    /**
     *
     * @return
     */
    public T getBody() {
        return result;
    }

}
