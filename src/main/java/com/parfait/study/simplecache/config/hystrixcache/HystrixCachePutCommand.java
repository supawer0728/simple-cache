package com.parfait.study.simplecache.config.hystrixcache;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

@Slf4j
public class HystrixCachePutCommand extends HystrixCommand<Object> {

    private final Cache delegate;
    private final Object key;
    private final Object value;

    public HystrixCachePutCommand(Cache delegate, Object key, Object value) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("testGroupKey"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("cache-put"))
                    .andCommandPropertiesDefaults(
                            HystrixCommandProperties.defaultSetter()
                                                    .withExecutionTimeoutInMilliseconds(500)
                                                    .withCircuitBreakerErrorThresholdPercentage(50)
                                                    .withCircuitBreakerRequestVolumeThreshold(5)
                                                    .withMetricsRollingStatisticalWindowInMilliseconds(20000)));
        this.delegate = delegate;
        this.key = key;
        this.value = value;
    }

    @Override
    protected Object run() {
        delegate.put(key, value);
        return null;
    }

    @Override
    protected Object getFallback() {
        log.warn("put fallback called, circuit is {}", super.circuitBreaker.isOpen() ? "opened" : "closed");
        return null;
    }
}
