package com.xforceplus.ultraman.permissions.jdbc.parser.http;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.xforceplus.ultraman.permissions.jdbc.parser.http.request.AuthRequest;
import com.xforceplus.ultraman.permissions.jdbc.parser.http.response.AuthResponse;
import com.xforceplus.ultraman.permissions.jdbc.utils.MD5Util;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): leo
 * 创建时间: 2020/9/2 10:02 AM
 */

@EnableConfigurationProperties(TenantConfig.class)
public class HttpClient {


    private OkHttpClient client;
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);
    private static final String TOKEN_KEY = "x-app-token";
    private Map<String, Pair> tokenMap = Maps.newConcurrentMap();
    private int tokenRefreshTime;


    private Gson gson = new Gson();


    public HttpClient(int connTimeOut, int readTimeOut, int tokenRefreshTIme) {
        this.tokenRefreshTime = tokenRefreshTIme;
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(connTimeOut, TimeUnit.SECONDS)
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .build();
    }


    /**
     * Get 请求
     *
     * @param url           address
     * @param authLoginName user
     * @param authPassword  password
     * @param authUrl
     * @return
     */
    public String doGet(String url, Map<String, String> params, String authLoginName,
                        String authPassword, String authUrl) {
        HttpEntity entity = buildHttpEntity(url, params, "",
                authLoginName, authPassword, authUrl);
        logger.info("Call url : {}  authUrl : {} authLoginName : {} authPassword : {}"
                , entity.getUrl(), entity.getAuthUrl(),
                entity.getAuthLoginName(), entity.getAuthPassword());
        authClient(entity.getAuthUrl(), entity.getAuthLoginName(), entity.getAuthPassword());
        Request.Builder builder = new Request.Builder();
        String urlParams = buildUrlParams(params);
        if (!StringUtils.isBlank(urlParams)) {
            builder.url(String.format("%s?%s", url, urlParams));
        } else {
            builder.url(url);
        }
        if (needAuth(authLoginName, authPassword, authUrl)) {
            builder.header(TOKEN_KEY, getToken(
                    entity.getAuthLoginName(), entity.getAuthPassword()).getRight().toString());
        }
        Request request = builder.build();
        String response = invokeRemote(request);
        return response;
    }

    private static String buildUrlParams(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }
            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }
        return result.toString();
    }

    private boolean needAuth(String authLoginName, String authPassword, String authUrl) {
        return !StringUtils.isBlank(authLoginName)
                && !StringUtils.isBlank(authPassword) && !StringUtils.isBlank(authUrl);
    }

    /**
     * Post请求
     *
     * @param
     * @param url           url地址
     * @param requestStr    requestBody[json]
     * @param authLoginName authLoginName
     * @param authPassword  authPassword
     * @param authUrl       authUrl
     * @return
     */
    public String doPost(String url, String requestStr, String authLoginName,
                         String authPassword, String authUrl) {
        HttpEntity entity = buildHttpEntity(url, null,
                requestStr, authLoginName, authPassword, authUrl);
        authClient(entity.getAuthUrl(), entity.getAuthLoginName(), entity.getAuthPassword());
        RequestBody requestBody = RequestBody.create(JSON, entity.getRequestStr());
        logger.info("Call url is {}", entity.getUrl());
        Request.Builder builder = new Request.Builder();
        builder.url(entity.getUrl()).post(requestBody);
        if (needAuth(authLoginName, authPassword, authUrl)) {
            builder.header(TOKEN_KEY, getToken(
                    entity.getAuthLoginName(), entity.getAuthPassword()).getRight().toString());
        }
        Request request = builder.build();
        String response = invokeRemote(request);
        return response;
    }

    private void authClient(String authUrl, String authLoginName, String authPassword) {
        if (StringUtils.isBlank(authUrl) || StringUtils.isBlank(authLoginName)
                || StringUtils.isBlank(authPassword)) {
            return;
        }
        String tokenKey = MD5Util.crypt(String.format("%s:%s", authLoginName, authPassword));
        Pair<Long, String> token = getToken(authLoginName, authPassword);
        if (token != null && System.currentTimeMillis() - token.getLeft() < tokenRefreshTime * 1000) {
            return;
        }
        AuthRequest authRequest = new AuthRequest();
        authRequest.setClientId(authLoginName);
        authRequest.setSecret(authPassword);
        Request request = new Request.Builder().url(authUrl)
                .post(RequestBody.create(JSON, gson.toJson(authRequest)))
                .build();
        String response = invokeRemote(request);
        AuthResponse authResponse = gson.fromJson(response, AuthResponse.class);
        token = Pair.of(System.currentTimeMillis(), authResponse.getData());
        tokenMap.put(tokenKey, token);
    }

    private String invokeRemote(Request request) {
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new HttpCallException(String.format("Http execute failed reason: %s",
                        response.message()));
            }
            return response.body().string();

        } catch (Throwable e) {
            throw new HttpCallException("Http execute failed ", e);
        }
    }

    private Pair getToken(String authLoginName, String authPassword) {
        String tokenKey = MD5Util.crypt(String.format("%s:%s", authLoginName, authPassword));
        Pair token = tokenMap.get(tokenKey);
        return token;
    }

    private HttpEntity buildHttpEntity(String url, Map<String, String> params,
                                       String requestStr, String authLoginName,
                                       String authPassword, String authUrl) {
        return HttpEntity.builder()
                .params(params)
                .url(url)
                .requestStr(requestStr)
                .authLoginName(authLoginName)
                .authPassword(authPassword)
                .authUrl(authUrl).build();
    }

}
