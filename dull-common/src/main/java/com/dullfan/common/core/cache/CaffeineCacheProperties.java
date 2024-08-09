package com.dullfan.common.core.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "cache.config")
public class CaffeineCacheProperties {
    /**
     * 缓存初始容量
     */
    private Integer initCacheCapacity = 256;
    /**
     * 缓存最大容量
     */
    private Integer maxCacheCapacity = 256;
    /**
     * 是否允许null作为缓存value
     */
    private Boolean allowNullValue = Boolean.TRUE;
}
