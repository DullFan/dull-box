package com.dullfan.system.bloom;

import java.util.Collection;

/**
 * 布隆过滤器管理器的顶级接口
 */
public interface BloomFilterManager {

    /**
     * 根据名称获取对应的布隆过滤器
     */
    BloomFilter getFilter(String name);

    /**
     * 获取目前管理中存在的布隆过滤器名称列表
     */
    Collection<String> getFilterNames();

}
