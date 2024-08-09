package com.dullfan.system.cache;

import com.dullfan.common.constant.CacheConstants;
import com.dullfan.common.exception.ServiceException;
import com.dullfan.system.mappers.ABaseMapper;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.io.Serializable;
import java.util.*;

/**
 * 手动处理缓存的公用接口
 */
public abstract class AbstractManualCacheService<V> implements ManualCacheService<V> {

    @Resource
    private CacheManager cacheManager;

    private final Object lock = new Object();

    protected abstract ABaseMapper getBaseMapper();

    @Override
    public V getById(Serializable id) {
        // 双重检查机制,防止一次性多个请求导致数据库崩溃
        V result = getByCache(id);
        if (Objects.nonNull(result)) {
            return result;
        }
        // 使用锁机制来避免缓存击穿问题
        synchronized (lock) {
            result = getByCache(id);
            if (Objects.nonNull(result)) {
                return result;
            }
        }
        result = getByDB(id);
        if (Objects.nonNull(result)) {
            putCache(id, result);
        }
        return result;
    }

    @Override
    public boolean updateById(Serializable id, V entity) {
        int rowNum = getBaseMapper().updateByIdCache(entity,(Long) id);
        removeCache(id);
        return rowNum == 1;
    }

    @Override
    public boolean removeById(Serializable id) {
        Integer rowNum = getBaseMapper().deleteByIdCache((Long) id);
        removeCache(id);
        return rowNum == 1;
    }

    @Override
    public List<V> getByIds(Collection<? extends Serializable> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return Lists.newArrayList();
        }
        List<V> result = ids.stream().map(this::getById).toList();
        if(CollectionUtils.isEmpty(result)){
            return Lists.newArrayList();
        }
        return result;
    }

    @Override
    public boolean updateByIds(Map<? extends Serializable, V> entityMap) {
        if(MapUtils.isEmpty(entityMap)){
            return false;
        }
        for (Map.Entry<? extends Serializable, V> entry : entityMap.entrySet()) {
            if(!updateById(entry.getKey(),entry.getValue())){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return false;
        }
        for (Serializable id : ids) {
            if(!removeById(id)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Cache getCache() {
        if (Objects.isNull(cacheManager)) {
            throw new ServiceException("缓存管理器为空");
        }
        return cacheManager.getCache(CacheConstants.BOX_CACHE_NAME);
    }

    /**
     * 将实体信息保存到缓存中
     */
    private void putCache(Serializable id, V result) {
        String cacheKey = getCacheKey(id);
        Cache cache = getCache();
        if(Objects.isNull(cache)){
            return;
        }
        if(Objects.isNull(cacheKey)){
            return;
        }
        cache.put(cacheKey,result);
    }

    /**
     * 根据ID查询实体信息
     */
    private V getByDB(Serializable id) {
        return (V) getBaseMapper().selectByIdCache((Long) id);
    }

    /**
     * 根据ID从缓存中获取实体
     */
    private V getByCache(Serializable id) {
        String cacheKey = getCacheKey(id);
        Cache cache = getCache();
        if (Objects.isNull(cache)) {
            return null;
        }
        Cache.ValueWrapper valueWrapper = cache.get(cacheKey);
        if (Objects.isNull(valueWrapper)) {
            return null;
        }

        return (V)valueWrapper.get();
    }

    /**
     * 生成对应的缓存key
     */
    private String getCacheKey(Serializable id) {
        return String.format(getKeyFormat(),id);
    }

    /**
     * 删除缓存信息
     */
    private void removeCache(Serializable id) {
        String cacheKey = getCacheKey(id);
        Cache cache = getCache();
        if(Objects.isNull(cache)){
            return;
        }
        cache.evict(cacheKey);
    }
}
