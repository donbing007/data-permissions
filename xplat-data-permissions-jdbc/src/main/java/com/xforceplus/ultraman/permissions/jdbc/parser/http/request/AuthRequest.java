package com.xforceplus.ultraman.permissions.jdbc.parser.http.request;




/**
 * @author leo
 */
public class AuthRequest
{
    private String clientId;
    private String secret;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
