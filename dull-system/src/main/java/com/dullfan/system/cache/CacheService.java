package com.dullfan.system.cache;

import java.io.Serializable;

/**
 * 支持业务缓存的顶级Service接口
 */
public interface CacheService<V> {

    /**
     * 根据ID查询实体信息
     */
    V getById(Serializable id);

    /**
     * 根据ID来更新缓存信息
     */
    boolean updateById(Serializable id,V entity);

    /**
     * 根据ID删除实体信息
     */
    boolean removeById(Serializable id);
}
