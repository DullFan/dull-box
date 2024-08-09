package com.dullfan.system.bloom.local;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 布隆过滤器配置
 */
@Component
@ConfigurationProperties(prefix = "local.bloom.filter")
@Data
public class LocalBloomFilterConfig {

    private List<LocalBloomFilterConfigItem> items;

}
