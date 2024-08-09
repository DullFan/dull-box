package com.dullfan.system.bloom.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 布隆过滤器拦截顶级接口
 */
public interface BloomFilterInterceptor extends HandlerInterceptor {

    /**
     * 拦截器的名称
     */
    String getName();

    /**
     * 要拦截的URL
     */
    String[] getPatterns();

    /**
     * 要过滤的URL
     */
    String[] getExcludePatterns();
}
