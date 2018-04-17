package com.parfait.study.simplecache.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.RedisCache;

import java.util.Random;

@Slf4j
public class QuarterFailRedisCache extends RedisCache {

    private final RedisCache delegate;
    private final Random random = new Random();
    private final int bound = 4;

    protected QuarterFailRedisCache(RedisCache delegate) {
        super(delegate.getName(), delegate.getNativeCache(), delegate.getCacheConfiguration());
        this.delegate = delegate;
    }

    @Override
    public void put(Object key, Object value) {
        log.info("QuarterFailRedisCache.put() called");
        checkRandom();
        delegate.put(key, value);
    }

    private void checkRandom() {
        int factor = random.nextInt(bound);
        log.info("QuarterFailRedisCache.factor : {}", factor);
        if (factor == 0) {
            throw new RuntimeException("put fail");
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        log.info("QuarterFailRedisCache.putIfAbsent() called");
        checkRandom();
        return delegate.putIfAbsent(key, value);
    }

    @Override
    public void evict(Object key) {
        log.info("QuarterFailRedisCache.evict() called");
        checkRandom();
        delegate.evict(key);
    }

    @Override
    public ValueWrapper get(Object key) {
        log.info("QuarterFailRedisCache.get(Object) called");
        checkRandom();
        return delegate.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        log.info("QuarterFailRedisCache.get(Object, Class) called");
        checkRandom();
        return delegate.get(key, type);
    }
}
