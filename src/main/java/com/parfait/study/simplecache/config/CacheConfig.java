package com.parfait.study.simplecache.config;

import com.parfait.study.simplecache.config.chaincache.ChainedCacheManager;
import com.parfait.study.simplecache.config.hystrixcache.HystrixCacheManager;
import com.parfait.study.simplecache.config.logcache.LoggingCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.io.IOException;
import java.time.Duration;

@EnableCaching(proxyTargetClass = true)
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Value("${redis.host:localhost}")
    private String redisHost;
    @Value("${redis.port:6379}")
    private Integer redisPort;
    @Value("${spring.cache.jcache.provider}")
    private String jCacheProvider;
    @Value("${spring.cache.jcache.config}")
    private Resource jCacheConfig;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }

    @Bean
    public CacheManager redisCacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory());

        RedisCacheConfiguration defaultConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                                       .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                                       .entryTtl(Duration.ofSeconds(20L));

        builder.cacheDefaults(defaultConfig);

        return new HystrixCacheManager(new LoggingCacheManager(builder.build(), "Global-Cache"));
    }

    @Bean
    public CacheManager jCacheCacheManager() {
        CachingProvider provider = Caching.getCachingProvider(jCacheProvider);

        try {
            return new LoggingCacheManager(new JCacheCacheManager(provider.getCacheManager(jCacheConfig.getURI(), provider.getDefaultClassLoader())), "Local-Cache");
        } catch (IOException e) {
            throw new IllegalStateException("can't create URI with spring.cache.jcache.config");
        }
    }

    @Bean
    @Primary
    @Override
    public CacheManager cacheManager() {

        return new ChainedCacheManager(jCacheCacheManager(), redisCacheManager());
    }
}
