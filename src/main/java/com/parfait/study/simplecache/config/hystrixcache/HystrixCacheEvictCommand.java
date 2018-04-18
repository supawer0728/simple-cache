package com.parfait.study.simplecache.config.hystrixcache;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

@Slf4j
public class HystrixCacheEvictCommand extends HystrixCommand<Object> {

    private final Cache delegate;
    private final Object key;

    public HystrixCacheEvictCommand(Cache delegate, Object key) {

        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("testGroupKey"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("cache-evict"))
                    .andCommandPropertiesDefaults(
                            HystrixCommandProperties.defaultSetter()
                                                    .withExecutionTimeoutInMilliseconds(500)
                                                    .withCircuitBreakerErrorThresholdPercentage(50)
                                                    .withCircuitBreakerRequestVolumeThreshold(5)
                                                    .withMetricsRollingStatisticalWindowInMilliseconds(20000)));
        this.delegate = delegate;
        this.key = key;
    }

    @Override
    protected Object run() {
        delegate.evict(key);
        return null;
    }

    @Override
    protected Object getFallback() {
        log.warn("evict fallback called, circuit is {}", super.circuitBreaker.isOpen() ? "opened" : "closed");
        return null;
    }
}
