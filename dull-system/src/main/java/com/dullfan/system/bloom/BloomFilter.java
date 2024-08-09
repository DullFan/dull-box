package com.dullfan.system.bloom;

/**
 * 布隆过滤器顶级接口
 */
public interface BloomFilter<T> {

    /**
     * 放入元素
     */
    boolean put(T object);

    /**
     * 判断元素是不是可能存在
     */
    boolean mightContain(T object);

    /**
     * 清空过滤器
     */
    void clear();

}
