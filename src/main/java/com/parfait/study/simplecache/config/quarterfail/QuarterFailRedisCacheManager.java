package com.parfait.study.simplecache.config.quarterfail;

import lombok.NonNull;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QuarterFailRedisCacheManager implements CacheManager {

    private final CacheManager delegate;
    private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>();

    public QuarterFailRedisCacheManager(@NonNull CacheManager cacheManager) {
        this.delegate = cacheManager;
    }

    @Override
    public Cache getCache(String name) {
        // TODO:  형안전성 체크
        return cacheMap.computeIfAbsent(name, key -> new QuarterFailRedisCache((RedisCache) delegate.getCache(name)));
    }

    @Override
    public Collection<String> getCacheNames() {
        return delegate.getCacheNames();
    }
}
