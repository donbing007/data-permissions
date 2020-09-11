package com.xforceplus.ultraman.permissions.jdbc.parser.http;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 2020/9/11 2:02 PM
 */
@ConfigurationProperties(prefix = "xplat.data.permissions.tenant")
@Configuration
public class TenantConfig {
    private String authLoginName;
    private String authPassword;
    private String authUrl;
    private Long tokenRefreshInSeconds;
    private String apiBaseUrl;
    private int connectionTimeOut;
    private int readTimeOut;

    public String getAuthLoginName() {
        return authLoginName;
    }

    public void setAuthLoginName(String authLoginName) {
        this.authLoginName = authLoginName;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public int getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(int connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public Long getTokenRefreshInSeconds() {
        return tokenRefreshInSeconds;
    }

    public void setTokenRefreshInSeconds(Long tokenRefreshInSeconds) {
        this.tokenRefreshInSeconds = tokenRefreshInSeconds;
    }
}
