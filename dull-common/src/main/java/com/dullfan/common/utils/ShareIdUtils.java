package com.dullfan.common.utils;

import com.dullfan.common.constant.Constants;

import java.util.Objects;

/**
 * 分享ID存储工具类
 */
public class ShareIdUtils {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程的分享ID
     */
    public static void set(Long shareId) {
        threadLocal.set(shareId);
    }

    /**
     * 获取当前线程的分享ID
     */
    public static Long get() {
        Long shareId = threadLocal.get();
        if (Objects.isNull(shareId)) {
            return Constants.ZERO_LONG;
        }
        return shareId;
    }
}