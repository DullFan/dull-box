package com.dullfan.framework.task.file;

import com.dullfan.common.entity.po.DeleteFile;
import com.dullfan.common.storage.StorageEngine;
import com.dullfan.common.utils.DateUtils;
import com.dullfan.common.utils.StringUtils;
import com.dullfan.common.utils.schedule.ScheduleTask;
import com.dullfan.system.entity.po.BoxFileChunk;
import com.dullfan.system.entity.query.BoxFileChunkQuery;
import com.dullfan.system.service.BoxFileChunkService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

/**
 * 过期分片清理任务
 */
@Component
@Slf4j
public class CleanExpireChunkFileTask implements ScheduleTask {
    private static final Long BATCH_SIZE = 1000L;

    @Resource
    StorageEngine storageEngine;


    @Resource
    private BoxFileChunkService boxFileChunkService;


    /**
     * 获取定时任务的名称
     */
    @Override
    public String getName() {
        return "CleanExpireChunkFileTask";
    }

    /**
     * 执行清理任务
     */
    @Override
    public void run() {
        log.info("{} 开始清理过期分片文件",getName());
        List<BoxFileChunk> expireFileChunkRecords;
        do {
            expireFileChunkRecords = scrollQueryExpireFileChunkRecords();
            if(CollectionUtils.isNotEmpty(expireFileChunkRecords)){
                deleteRealChunkFiles(expireFileChunkRecords);
                deleteChunkFileRecords(expireFileChunkRecords);
            }
        }while (CollectionUtils.isNotEmpty(expireFileChunkRecords));
        log.info("{} 清理完成过期分片文件",getName());
    }

    /**
     * 滚动查询过期的文件分片文件
     */
    private List<BoxFileChunk> scrollQueryExpireFileChunkRecords() {
        BoxFileChunkQuery boxFileChunkQuery = new BoxFileChunkQuery();
        boxFileChunkQuery.setExpirationTimeEnd(DateUtils.getNowDate());
        boxFileChunkQuery.setLimitCount(BATCH_SIZE);
        return boxFileChunkService.selectListByParam(boxFileChunkQuery);
    }

    /**
     * 物理删除过期文件分片
     */
    private void deleteRealChunkFiles(List<BoxFileChunk> expireFileChunkRecords) {
        DeleteFile deleteFile = new DeleteFile();
        List<String> list = expireFileChunkRecords.stream().map(BoxFileChunk::getRealPath).toList();
        deleteFile.setRealFilePathList(list);
        try {
            storageEngine.delete(deleteFile);
        } catch (IOException e) {
            log.error(StringUtils.format("定时删除文件分片失败,目录:{}",list));
        }
    }

    /**
     * 删除过期文件分片记录
     */
    private void deleteChunkFileRecords(List<BoxFileChunk> expireFileChunkRecords) {
        List<Long> list = null;
        try {
            list = expireFileChunkRecords.stream().map(BoxFileChunk::getId).toList();
            boxFileChunkService.deleteByIdBatch(list);
        } catch (Exception e) {
            log.error(StringUtils.format("定时删除文件分片失败,目录:{}",list));
        }
    }
}
