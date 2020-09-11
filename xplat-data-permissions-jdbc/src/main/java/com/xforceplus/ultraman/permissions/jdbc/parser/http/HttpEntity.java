package com.xforceplus.ultraman.permissions.jdbc.parser.http;


import java.util.Map;


/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 2020/9/11 2:02 PM
 */
public class HttpEntity {

    private String url;
    private String requestStr;
    private String authLoginName;
    private String authPassword;
    private String authUrl;
    private Map<String, String> params;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestStr() {
        return requestStr;
    }

    public void setRequestStr(String requestStr) {
        this.requestStr = requestStr;
    }

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

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    HttpEntity(String url, Map<String, String> params, String requestStr,
               String authLoginName, String authPassword, String authUrl) {
        this.url = url;
        this.requestStr = requestStr;
        this.authLoginName = authLoginName;
        this.authPassword = authPassword;
        this.authUrl = authUrl;
        this.params = params;
    }

    public static HttpEntity.HttpEntityBuilder builder() {
        return new HttpEntity.HttpEntityBuilder();
    }

    /**
     * HttpEntityBuilder
     */
    public static class HttpEntityBuilder {
        private String url;
        private String requestStr;
        private String authLoginName;
        private String authPassword;
        private String authUrl;
        private Map<String, String> params;

        HttpEntityBuilder() {
        }

        public HttpEntity.HttpEntityBuilder params(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public HttpEntity.HttpEntityBuilder url(String url) {
            this.url = url;
            return this;
        }

        public HttpEntity.HttpEntityBuilder requestStr(String requestStr) {
            this.requestStr = requestStr;
            return this;
        }

        public HttpEntity.HttpEntityBuilder authLoginName(String authLoginName) {
            this.authLoginName = authLoginName;
            return this;
        }

        public HttpEntity.HttpEntityBuilder authPassword(String authPassword) {
            this.authPassword = authPassword;
            return this;
        }

        public HttpEntity.HttpEntityBuilder authUrl(String authUrl) {
            this.authUrl = authUrl;
            return this;
        }

        public HttpEntity build() {
            return new HttpEntity(this.url, this.params, this.requestStr,
                    this.authLoginName, this.authPassword, this.authUrl);
        }

        @Override
        public String toString() {
            return "HttpEntity.HttpEntityBuilder(url=" + this.url + ", requestStr="
                    + this.requestStr + ", authLoginName=" + this.authLoginName
                    + ", authPassword=" + this.authPassword + ", authUrl=" + this.authUrl + ")";
        }
    }


}
