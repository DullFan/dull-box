package com.dullfan.system.aspect;

import com.dullfan.common.constant.CacheConstants;
import com.dullfan.common.core.redis.RedisCache;
import com.dullfan.common.entity.vo.Result;
import com.dullfan.common.exception.ServiceException;
import com.dullfan.common.utils.ShareIdUtils;
import com.dullfan.system.entity.po.BoxShare;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * 统一的分享码校验切面逻辑实现类
 */
@Component
@Aspect
@Slf4j
public class ShareCodeAspect {

    /**
     * 登录认证参数名称
     */
    private static final String SHARE_CODE_AUTH_PARAM_NAME = "shareToken";

    /**
     * 请求头登录认证key
     */
    private static final String SHARE_CODE_AUTH_REQUEST_HEADER_NAME = "Share-Token";

    /**
     * 切点表达式
     */
    private final static String POINT_CUT = "@annotation(com.dullfan.common.annotation.NeedShareCode)";

    @Resource
    private RedisCache redisCache;

    /**
     * 切点模版方法
     */
    @Pointcut(value = POINT_CUT)
    public void shareCodeAuth() {

    }

    /**
     * 校验分享码
     */
    @Around("shareCodeAuth()")
    public Object shareCodeAuthAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String requestURI = request.getRequestURI();
        log.info("成功拦截到请求，URI为：{}", requestURI);
        if (!checkAndSaveShareId(request)) {
            log.warn("成功拦截到请求，URI为：{}. 检测到用户的分享码失效，将跳转至分享码校验页面", requestURI);
            throw new ServiceException("分享码错误");
        }
        log.info("成功拦截到请求，URI为：{}，请求通过", requestURI);
        return proceedingJoinPoint.proceed();
    }

    /**
     * 校验token并提取shareId
     */
    private boolean checkAndSaveShareId(HttpServletRequest request) {
        String shareToken = request.getHeader(SHARE_CODE_AUTH_REQUEST_HEADER_NAME);
        if (StringUtils.isBlank(shareToken)) {
            shareToken = request.getParameter(SHARE_CODE_AUTH_PARAM_NAME);
        }
        if (StringUtils.isBlank(shareToken)) {
            return false;
        }
        BoxShare boxShare = redisCache.getCacheObject(CacheConstants.SHARE_CODE_KEY + shareToken);
        if (Objects.isNull(boxShare)) {
            return false;
        }
        saveShareId(boxShare.getShareId());
        return true;
    }

    /**
     * 保存分享ID到线程上下文中
     */
    private void saveShareId(Object shareId) {
        ShareIdUtils.set(Long.valueOf(String.valueOf(shareId)));
    }

}
