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

    private final int m_code;
    private final T m_body;
    private String m_message;

    /**
     *
     * @param m_code
     * @param message
     */
    public HttpResponse(int m_code, String message, T body) {
        this.m_code = m_code;
        this.m_body = body;
        this.m_message = message;
    }

    public int getStatusCode() {
        return m_code;
    }

    /**
     *
     * @return
     */
    public T getBody() {
        return m_body;
    }

}
