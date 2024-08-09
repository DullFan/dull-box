package com.dullfan.framework.task.bloom;

import com.dullfan.common.utils.schedule.ScheduleManage;
import com.dullfan.framework.task.file.CleanExpireChunkFileTask;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 定时重建简单分享详情布隆过滤器任务触发器
 */
@Slf4j
@Component
public class RebuildShareSimpleDetailBloomFilterLauncher implements CommandLineRunner {

    /**
     * 每天00:00:01触发一次任务
     */
    private final static String CRON = "1 0 0 * * ? ";

    @Resource
    private RebuildShareSimpleDetailBloomFilterTask task;

    @Resource
    private ScheduleManage scheduleManage;

    @Override
    public void run(String... args) throws Exception {
        scheduleManage.startTask(task,CRON);
        log.info("定时清除过期分片文件任务启动");
    }
}
