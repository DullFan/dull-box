package com.dullfan.system.bloom.interceptor;

import com.dullfan.system.bloom.BloomFilter;
import com.dullfan.system.bloom.BloomFilterManager;
import com.dullfan.common.exception.ServiceException;
import com.dullfan.common.utils.encryption.IdUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 查询简单分享详情布隆过滤器拦截器
 */
@Slf4j
@Component
public class ShareSimpleDetailBloomFilterInterceptor implements BloomFilterInterceptor{

    @Resource
    private BloomFilterManager manager;

    private static final String BLOOM_FILTER_NAME = "SHARE_SIMPLE_DETAIL";

    @Override
    public String getName() {
        return "ShareSimpleDetailBloomFilterInterceptor";
    }

    @Override
    public String[] getPatterns() {
        return ArrayUtils.toArray("/share/simple");
    }

    @Override
    public String[] getExcludePatterns() {
        return new String[0];
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String encShareId = request.getParameter("shareId");
        if(StringUtils.isBlank(encShareId)){
            throw new ServiceException("分享ID不能为空");
        }
        BloomFilter bloomFilter = manager.getFilter(BLOOM_FILTER_NAME);
        if(Objects.isNull(bloomFilter)){
            log.info("分享简单详情布隆过滤器为null");
            return true;
        }
        Long shareId = IdUtil.decrypt(encShareId);
        boolean mightContain = bloomFilter.mightContain(shareId);
        if(mightContain){
            log.info("分享简单详情布隆过滤器执行");
            return true;
        }
        log.info("分享简单详情布隆过滤器中没有这个key");
        throw new ServiceException("分享已被取消");
    }
}
