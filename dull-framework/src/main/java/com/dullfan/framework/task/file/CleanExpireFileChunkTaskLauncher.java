package com.dullfan.framework.task.file;

import com.dullfan.common.utils.schedule.ScheduleManage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 定时清理过期的文件分片任务触发器
 */
@Slf4j
@Component
public class CleanExpireFileChunkTaskLauncher implements CommandLineRunner {

    /**
     * 每天00:00:01触发一次任务
     */
    private final static String CRON = "1 0 0 * * ? ";

    @Resource
    private CleanExpireChunkFileTask task;

    @Resource
    private ScheduleManage scheduleManage;

    @Override
    public void run(String... args) throws Exception {
        scheduleManage.startTask(task,CRON);
        log.info("定时清除过期分片文件任务启动");
    }
}
