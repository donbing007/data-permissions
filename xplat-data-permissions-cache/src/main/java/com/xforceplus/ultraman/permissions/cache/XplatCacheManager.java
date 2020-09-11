package com.xforceplus.ultraman.permissions.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 2020/9/8 06:23 PM
 */
public class XplatCacheManager extends CaffeineCacheManager implements InitializingBean {
    private Map<String, String> cacheSpecs = new HashMap<>();

    private Map<String, Caffeine<Object, Object>> builders = new HashMap<>();

    private CacheLoader cacheLoader;

    @Override
    public void afterPropertiesSet() throws Exception {
        for (Map.Entry<String, String> cacheSpecEntry : cacheSpecs.entrySet()) {
            builders.put(cacheSpecEntry.getKey(), Caffeine.from(cacheSpecEntry.getValue()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Cache<Object, Object> createNativeCaffeineCache(String name) {
        Caffeine<Object, Object> builder = builders.get(name);
        if (builder == null) {
            return super.createNativeCaffeineCache(name);
        }

        if (this.cacheLoader != null) {
            return builder.build(this.cacheLoader);
        } else {
            return builder.build();
        }
    }

    public Map<String, String> getCacheSpecs() {
        return cacheSpecs;
    }

    public void setCacheSpecs(Map<String, String> cacheSpecs) {
        this.cacheSpecs = cacheSpecs;
    }

    @Override
    public void setCacheLoader(CacheLoader cacheLoader) {
        super.setCacheLoader(cacheLoader);
        this.cacheLoader = cacheLoader;
    }
}
