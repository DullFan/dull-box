package com.dullfan.system.listener.share;

import com.dullfan.common.enums.DelFlagEnum;
import com.dullfan.system.entity.po.BoxUserFile;
import com.dullfan.system.event.file.DeleteFileEvent;
import com.dullfan.system.event.file.FileRestoreEvent;
import com.dullfan.system.service.BoxUserFileService;
import com.dullfan.system.service.ShareService;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 监听分享状态改变事件
 */
@Component
public class ShareStatusChangeListener {

    @Resource
    private BoxUserFileService boxUserFileService;

    @Resource
    private ShareService shareService;

    /**
     * 监听文件删除事件,刷新所有受影响的分享状态
     */
    @EventListener(DeleteFileEvent.class)
    @Async(value = "eventListenerTaskExecutor")
    public void changeShare2FileDelete(DeleteFileEvent event) {
        List<Long> fileIdList = event.getFileIdList();
        if(CollectionUtils.isEmpty(fileIdList)){
            return;
        }
        List<BoxUserFile> allFileRecordsByFileId = boxUserFileService.findAllFileRecordsByFileId(fileIdList);
        List<Long> allAvailableFileIdList = allFileRecordsByFileId.stream()
                .filter(record -> Objects.equals(record.getDelFlag(), DelFlagEnum.NO.getCode()))
                .map(BoxUserFile::getFileId)
                .collect(Collectors.toList());
        allAvailableFileIdList.addAll(fileIdList);
        shareService.refreshShareStatus(allAvailableFileIdList);
    }

    /**
     * 监听文件还原事件,刷新所有受影响的分享状态
     */
    @EventListener(FileRestoreEvent.class)
    @Async(value = "eventListenerTaskExecutor")
    public void changeShare2Normal(FileRestoreEvent event) {
        List<Long> fileIdList = event.getFileIdList();
        if(CollectionUtils.isEmpty(fileIdList)){
            return;
        }
        List<BoxUserFile> allFileRecordsByFileId = boxUserFileService.findAllFileRecordsByFileId(fileIdList);
        List<Long> allAvailableFileIdList = allFileRecordsByFileId.stream()
                .filter(record -> Objects.equals(record.getDelFlag(), DelFlagEnum.NO.getCode()))
                .map(BoxUserFile::getFileId)
                .collect(Collectors.toList());
        allAvailableFileIdList.addAll(fileIdList);
        shareService.refreshShareStatus(allAvailableFileIdList);
    }
}
