package com.xforceplus.ultraman.permissions.jdbc.parser.http.response;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 2020-09-02 17:19
 */
public class AuthResponse {

    private long code;
    private String message;
    private String data;

    public long getCode() { return code; }
    public void setCode(long value) { this.code = value; }

    public String getMessage() { return message; }
    public void setMessage(String value) { this.message = value; }

    public String getData() { return data; }
    public void setData(String value) { this.data = value; }
}
