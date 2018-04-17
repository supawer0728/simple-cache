package com.parfait.study.simplecache.config.logcache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

@Slf4j
public class LoggingCache implements Cache {

    private final Cache delegate;
    private final String name;

    public LoggingCache(Cache delegate, String name) {
        this.delegate = delegate;
        this.name = name;
    }

    @Override
    public String getName() {
        log.info("{}.getName() called", name);
        return delegate.getName();
    }

    @Override
    public Object getNativeCache() {
        log.info("{}.getNativeCache() called", name);
        return delegate.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        log.info("{}.get(Object) called", name);
        return delegate.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        log.info("{}.get(Object, Class) called", name);
        return delegate.get(key, type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        log.info("{}.get(Object, Callable) called", name);
        return delegate.get(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        log.info("{}.put(Object, Object) called", name);
        delegate.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        log.info("{}.putIfAbsent(Object, Object) called", name);
        return delegate.putIfAbsent(key, value);
    }

    @Override
    public void evict(Object key) {
        log.info("{}.evict(Object) called", name);
        delegate.evict(key);
    }

    @Override
    public void clear() {
        log.info("{}.clear(Object) called", name);
        delegate.clear();
    }
}
