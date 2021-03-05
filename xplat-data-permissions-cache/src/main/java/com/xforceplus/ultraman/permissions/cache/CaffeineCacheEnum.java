package com.xforceplus.ultraman.permissions.cache;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 2020/9/8 05:13 PM
 */
public enum CaffeineCacheEnum {


    DEFAULT_CACHE_NAME("DEFAULT_CACHE_NAME","initialCapacity=100,maximumSize=300,expireAfterWrite=300s"),
    USER_AUTHORIZATION_TAX("USER_AUTHORIZATION_TAX_CACHE", "initialCapacity=50,maximumSize=500,expireAfterWrite=600s"),
    USER_AUTHORIZATION_COMPANY("USER_AUTHORIZATION_COMPANY_CACHE", "initialCapacity=50,maximumSize=500,expireAfterWrite=600s");

    private String cacheName;
    private String cacheSpec;

    CaffeineCacheEnum(String cacheName, String cacheSpec) {
        this.cacheName = cacheName;
        this.cacheSpec = cacheSpec;
    }

    public String cacheName() {
        return this.cacheName;
    }

    public String cacheSpec() {
        return this.cacheSpec;
    }
}
