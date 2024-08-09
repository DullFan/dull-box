package com.dullfan.system.bloom.local;

import com.dullfan.system.bloom.BloomFilter;
import com.dullfan.system.bloom.BloomFilterManager;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class LocalBloomFilterManager implements BloomFilterManager, InitializingBean {

    @Resource
    private LocalBloomFilterConfig config;

    /**
     * 容器
     */
    private final Map<String, BloomFilter> bloomFilterContainer = Maps.newConcurrentMap();

    @Override
    public BloomFilter getFilter(String name) {
        return bloomFilterContainer.get(name);
    }

    @Override
    public Collection<String> getFilterNames() {
        return bloomFilterContainer.keySet();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<LocalBloomFilterConfigItem> items = config.getItems();
        if(CollectionUtils.isNotEmpty(items)){
            items.forEach(item -> {
                String funnelTypeName = item.getFunnelTypeName();
                try {
                    FunnelType funnelType = FunnelType.valueOf(funnelTypeName);
                    bloomFilterContainer.putIfAbsent(item.getName(),new LocalBloomFilter(funnelType.getFunnel(),item.getExpectedInsertions(),item.getFpp()));
                } catch (Exception e){
                    log.error("布隆过滤器初始化失败，{}",e.getMessage());
                }
            });
        }
    }
}
