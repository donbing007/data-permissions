package com.xforceplus.ultraman.permissions.jdbc.parser.impl;

import com.google.common.collect.Maps;
import com.xforceplus.ultraman.permissions.jdbc.parser.AuthorizedUserService;
import com.xforceplus.ultraman.permissions.jdbc.parser.http.HttpClient;
import com.xforceplus.ultraman.permissions.jdbc.parser.http.TenantConfig;
import com.xforceplus.ultraman.permissions.jdbc.parser.http.response.GetUserCompany;
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

    private static final String GET_USER_COMPANY_INFO_PATH = "/users/%s/tax-nums";


    private static final String USER_AUTHORIZATION_TAX_CACHE = "USER_AUTHORIZATION_TAX_CACHE";

    @Autowired
    private HttpClient client;

    @Override
    @Cacheable(cacheNames = {USER_AUTHORIZATION_TAX_CACHE}, key = "#userId")
    public Set<String> getUserTaxNums(Long userId) {
        Map<String, String> params = Maps.newHashMap();
        String response = client.doGet(String.format("%s%s", config.getApiBaseUrl()
                , String.format(GET_USER_COMPANY_INFO_PATH, userId)), params, config.getAuthLoginName()
                , config.getAuthPassword(), config.getAuthUrl());
        HttpResponse<List<GetUserCompany>> companyResponse = GsonUtils.fromJsonString(response, GetUserCompany.class);
        return companyResponse.getBody().stream().map(item -> item.getTaxNo()).collect(Collectors.toSet());
    }
}
