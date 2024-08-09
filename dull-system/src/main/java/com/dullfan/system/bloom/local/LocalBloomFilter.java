package com.dullfan.system.bloom.local;

import com.dullfan.system.bloom.BloomFilter;
import com.google.common.hash.Funnel;

/**
 * 本地实现的布隆过滤器
 */
public class LocalBloomFilter<T> implements BloomFilter<T> {

    private com.google.common.hash.BloomFilter delegate;

    /**
     * 数据类型通道
     */
    private Funnel funnel;

    /**
     * 数组的长度
     */
    private long expectedInsertions;

    /**
     * 误判率
     */
    private double fpp;

    public LocalBloomFilter(Funnel funnel, long expectedInsertions, double fpp) {
        this.funnel = funnel;
        this.expectedInsertions = expectedInsertions;
        this.fpp = fpp;
        this.delegate = com.google.common.hash.BloomFilter.create(funnel, expectedInsertions, fpp);
    }

    /**
     * 放入元素
     */
    @Override
    public boolean put(T object) {
        return delegate.put(object);
    }

    /**
     * 放入元素
     */
    @Override
    public boolean mightContain(T object) {
        return delegate.mightContain(object);
    }

    /**
     * 放入元素
     */
    @Override
    public void clear() {
        this.delegate = com.google.common.hash.BloomFilter.create(funnel, expectedInsertions, fpp);
    }
}
