package com.dullfan.system.cache.share;

import com.dullfan.system.cache.AbstractManualCacheService;
import com.dullfan.system.entity.po.BoxShare;
import com.dullfan.system.mappers.ABaseMapper;
import com.dullfan.system.mappers.BoxShareMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 手动缓存实现分享业务的服务
 */
@Component(value = "shareManualCacheService")
public class ShareCacheService extends AbstractManualCacheService<BoxShare> {

    @Resource
    private BoxShareMapper boxShareMapper;

    @Override
    protected ABaseMapper getBaseMapper() {
        return boxShareMapper;
    }

    /**
     * 获取缓存的key的格式
     */
    @Override
    public String getKeyFormat() {
        return "SHARE:ID:%s";
    }
}
