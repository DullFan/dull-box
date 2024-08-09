package com.dullfan.framework.task.bloom;

import com.dullfan.common.utils.schedule.ScheduleTask;
import com.dullfan.system.bloom.BloomFilter;
import com.dullfan.system.bloom.BloomFilterManager;
import com.dullfan.system.service.ShareService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 定时重建简单分享详情布隆过滤器任务
 */
@Slf4j
@Component
public class RebuildShareSimpleDetailBloomFilterTask implements ScheduleTask {

    @Resource
    private BloomFilterManager manager;

    @Resource
    private ShareService shareService;

    private static final String BLOOM_FILTER_NAME = "SHARE_SIMPLE_DETAIL";

    @Override
    public String getName() {
        return "RebuildShareSimpleDetailBloomFilterTask";
    }

    /**
     * 执行重置任务
     */
    @Override
    public void run() {
        log.info("重置布隆过滤器: {}", BLOOM_FILTER_NAME);
        BloomFilter<Long> bloomFilter = manager.getFilter(BLOOM_FILTER_NAME);
        if(Objects.isNull(bloomFilter)){
            log.info("布隆过滤器重置失败: {}", BLOOM_FILTER_NAME);
            return;
        }
        bloomFilter.clear();

        long startId = 0L;
        long limit = 10000L;
        AtomicLong addCount = new AtomicLong(0L);

        List<Long> shareIdList;

        do {
            shareIdList = shareService.rollingQueryShareId(startId,limit);
            if(CollectionUtils.isNotEmpty(shareIdList)){
                shareIdList.forEach(item ->{
                    bloomFilter.put(item);
                    addCount.incrementAndGet();
                });
                startId = shareIdList.get(shareIdList.size() - 1);
            }
        } while (CollectionUtils.isNotEmpty(shareIdList));

        log.info("布隆过滤器重置完成: {}", BLOOM_FILTER_NAME);
    }
}
