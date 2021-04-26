package com.xforceplus.ultraman.permissions.jdbc.parser.impl;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.reflect.TypeToken;
import com.xforceplus.ultraman.permissions.jdbc.parser.AuthorizedUserService;
import com.xforceplus.ultraman.permissions.jdbc.parser.http.HttpClient;
import com.xforceplus.ultraman.permissions.jdbc.parser.http.TenantConfig;
import com.xforceplus.ultraman.permissions.jdbc.parser.http.dto.CompanyInfo;
import com.xforceplus.ultraman.permissions.jdbc.parser.http.dto.OrgResult;
import com.xforceplus.ultraman.permissions.jdbc.parser.http.response.HttpResponse;
import com.xforceplus.ultraman.permissions.jdbc.utils.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 2020/9/8 11:33 PM
 */
@EnableConfigurationProperties(TenantConfig.class)
public class AuthorizedUserServiceImpl implements AuthorizedUserService {

    @Autowired
    private TenantConfig config;

    private static final String DEFAULT_ROW_SIZE = "1000";
    private static final int DEFAULT_START_PAGE = 1;

    private static final String GET_USER_COMPANY_INFO_PATH = "/api/global/users/%s/tax-nums";
    private static final String GET_USER_COMPANY_ID_PATH = "/api/global/users/%s/companies";
    private static final String GET_USER_ORG_ID_PATH = "/api/global/orgs";



    private static final String USER_AUTHORIZATION_TAX_CACHE = "USER_AUTHORIZATION_TAX_CACHE";
    private static final String USER_AUTHORIZATION_COMPANY_CACHE = "USER_AUTHORIZATION_COMPANY_CACHE";
    private static final String USER_AUTHORIZATION_ORG_CACHE = "USER_AUTHORIZATION_ORG_CACHE";

    @Autowired
    private HttpClient client;

    @Override
    @Cacheable(cacheNames = {USER_AUTHORIZATION_ORG_CACHE}, key = "#userId")
    public Set<Long> getUserOrgIds(Long userId) {
        Map<String, String> params = Maps.newHashMap();
        int current = DEFAULT_START_PAGE;
        params.put("page", String.valueOf(current));
        params.put("row", DEFAULT_ROW_SIZE);
        params.put("userId", String.valueOf(userId));
        Set<Long> result = Sets.newHashSet();
        String response = client.doGet(String.format("%s%s", config.getApiBaseUrl()
                , GET_USER_ORG_ID_PATH), params, config.getAuthLoginName()
                , config.getAuthPassword(), config.getAuthUrl());
        HttpResponse<OrgResult> companyResponse = GsonUtils.fromJsonString(response,
                new TypeToken<HttpResponse<OrgResult>>(){}.getType());
        if(companyResponse.getBody() != null) {
            result.addAll(companyResponse.getBody().getContent().stream().map(item -> item.getOrgId()).collect(Collectors.toSet()));
        }
        while (companyResponse.getBody() != null && !companyResponse.getBody().isLast()) {
            current++;
            params.put("page", String.valueOf(current));
            String res = client.doGet(String.format("%s%s", config.getApiBaseUrl()
                    , GET_USER_ORG_ID_PATH), params, config.getAuthLoginName()
                    , config.getAuthPassword(), config.getAuthUrl());
            companyResponse = GsonUtils.fromJsonString(res,
                    new TypeToken<HttpResponse<OrgResult>>(){}.getType());
            result.addAll(companyResponse.getBody().getContent().stream().map(item->item.getOrgId()).collect(Collectors.toSet()));
        }
        return result;
    }

    @Override
    @Cacheable(cacheNames = {USER_AUTHORIZATION_TAX_CACHE}, key = "#userId")
    public Set<String> getUserTaxNums(Long userId) {
        Map<String, String> params = Maps.newHashMap();
        String response = client.doGet(String.format("%s%s", config.getApiBaseUrl()
                , String.format(GET_USER_COMPANY_INFO_PATH, userId)), params, config.getAuthLoginName()
                , config.getAuthPassword(), config.getAuthUrl());
        HttpResponse<List<String>> companyResponse = GsonUtils.fromJsonString(response, HttpResponse.class);
        if (companyResponse.getBody() != null) {
            return companyResponse.getBody().stream().collect(Collectors.toSet());
        }
        else {
            return Sets.newHashSet();
        }
    }

    @Override
    @Cacheable(cacheNames = {USER_AUTHORIZATION_COMPANY_CACHE}, key = "#userId")
    public Set<Long> getUserCompanyIds(Long userId) {
        Map<String, String> params = Maps.newHashMap();
        String response = client.doGet(String.format("%s%s", config.getApiBaseUrl()
                , String.format(GET_USER_COMPANY_ID_PATH, userId)), params, config.getAuthLoginName()
                , config.getAuthPassword(), config.getAuthUrl());
        HttpResponse<List<CompanyInfo>> companyInfoHttpResponse =
                GsonUtils.fromJsonString(response, new TypeToken<HttpResponse<List<CompanyInfo>>>() {
                }.getType());
        if (companyInfoHttpResponse.getBody() != null) {
            return companyInfoHttpResponse.getBody().stream().map(item -> item.getCompanyId()).collect(Collectors.toSet());
        }
        else {
            return Sets.newHashSet();
        }
    }
}
