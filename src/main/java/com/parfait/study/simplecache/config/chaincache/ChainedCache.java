package com.parfait.study.simplecache.config.chaincache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public class ChainedCache implements Cache {

    private final List<Cache> caches;
    private final int first = 0;
    private final int size;

    public ChainedCache(List<Cache> caches) {
        this.caches = Collections.unmodifiableList(caches);
        this.size = caches.size();
    }

    @Override
    public String getName() {
        return caches.get(first).getName();
    }

    @Override
    public Object getNativeCache() {
        throw new UnsupportedOperationException("getNativeCache");
    }

    @Override
    public ValueWrapper get(Object key) {

        for (int i = first; i < size; i++) {

            ValueWrapper valueWrapper = caches.get(i).get(key);

            if (valueWrapper != null && valueWrapper.get() != null) {
                if (i == first) {
                    return valueWrapper;
                }
                putIntoPreviousIndexedCaches(i, key, valueWrapper.get());
                return valueWrapper;
            }
        }

        return null;
    }

    private void putIntoPreviousIndexedCaches(int index, Object key, Object value) {
        for (int i = index - 1; i >= first; i--) {
            singleCachePut(caches.get(i), key, value);
        }
    }

    @Override
    public <T> T get(Object key, Class<T> type) {

        for (int i = first; i < size; i++) {
            T result = caches.get(i).get(key, type);
            if (result != null) {
                if (i == first) {
                    return result;
                }
                putIntoPreviousIndexedCaches(i, key, result);
                return result;
            }
        }

        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        throw new UnsupportedOperationException("get(Object, Callable)");
    }

    private void singleCachePut(Cache cache, Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void put(Object key, Object value) {
        for (Cache cache : caches) {
            cache.put(key, value);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        for (Cache cache : caches) {
            cache.putIfAbsent(key, value);
        }
        return new SimpleValueWrapper(value);
    }

    @Override
    public void evict(Object key) {
        for (Cache cache : caches) {
            cache.evict(key);
        }
    }

    @Override
    public void clear() {
        for (Cache cache : caches) {
            cache.clear();
        }
    }
}
