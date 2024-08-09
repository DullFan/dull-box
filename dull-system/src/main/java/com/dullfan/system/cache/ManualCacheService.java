package com.dullfan.system.cache;

import org.springframework.cache.Cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 手动缓存处理Service接口
 */
public interface ManualCacheService<V> extends CacheService<V> {

    /**
     * 根据主键集合获取缓存
     */
    List<V> getByIds(Collection<? extends Serializable> ids);

    /**
     * 批量更新实体缓存
     */
    boolean updateByIds(Map<? extends Serializable,V> entityMap);

    /**
     * 根据主键集合删除缓存
     */
    boolean removeByIds(Collection<? extends Serializable> ids);

    /**
     * 获取缓存key的模板信息
     */
    String getKeyFormat();

    /**
     * 获取缓存对象实体
     */
    Cache getCache();

}
