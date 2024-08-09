package com.dullfan.common.annotation;

import java.lang.annotation.*;

/**
 * 需要分享码校验的接口
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface NeedShareCode {
}
