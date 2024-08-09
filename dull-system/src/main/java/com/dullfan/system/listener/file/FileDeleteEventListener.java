package com.dullfan.system.listener.file;

import com.alibaba.fastjson2.JSONObject;
import com.dullfan.common.constant.Constants;
import com.dullfan.common.entity.po.DeleteFile;
import com.dullfan.common.enums.FolderFlagEnum;
import com.dullfan.common.exception.ServiceException;
import com.dullfan.common.storage.StorageEngine;
import com.dullfan.system.entity.po.BoxFile;
import com.dullfan.system.entity.po.BoxUserFile;
import com.dullfan.system.entity.query.BoxUserFileQuery;
import com.dullfan.system.event.file.FileDeleteEvent;
import com.dullfan.system.service.BoxFileService;
import com.dullfan.system.service.BoxUserFileService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 文件物理删除监听器
 */
@Component
@Slf4j
public class FileDeleteEventListener {

    @Resource
    private BoxFileService boxFileService;

    @Resource
    private StorageEngine storageEngine;

    @Resource
    private BoxUserFileService boxUserFileService;

    @EventListener(classes = FileDeleteEvent.class)
    @Async(value = "eventListenerTaskExecutor")
    public void deleteFile(FileDeleteEvent fileDeleteEvent){
        List<BoxUserFile> fileList = fileDeleteEvent.getFileList();
        if(CollectionUtils.isEmpty(fileList)){
            return;
        }
        List<Long> realFileIdList = findAllUnUseRealFileIdList(fileList);
        if(CollectionUtils.isEmpty(realFileIdList)){
            return;
        }
        List<BoxFile> boxFileList = boxFileService.selectByFileIds(realFileIdList);
        if(CollectionUtils.isEmpty(boxFileList)){
            return;
        }
        if(Objects.equals(boxFileService.deleteByFileIds(realFileIdList), Constants.ZERO_INT)){
            throw new ServiceException("文件物理删除失败,请手动删除"+ JSONObject.toJSONString(realFileIdList));
        }
        deDeleteFile(boxFileList);
    }

    private void deDeleteFile(List<BoxFile> fileList) { 
        List<String> realPathList = fileList.stream().map(BoxFile::getRealPath).toList();
        DeleteFile deleteFile = new DeleteFile();
        deleteFile.setRealFilePathList(realPathList);
        try {
            storageEngine.delete(deleteFile);
        } catch (IOException e) {
            throw new ServiceException("文件物理删除失败,请手动删除"+ JSONObject.toJSONString(fileList));
        }
    }

    /**
     * 查找所有没有被引用文件ID
     */
    private List<Long> findAllUnUseRealFileIdList(List<BoxUserFile> fileList) {
        return fileList.stream()
                .filter(record -> Objects.equals(record.getFolderFlag(), FolderFlagEnum.NO.getCode()))
                .filter(this::isUnused)
                .map(BoxUserFile::getRealFileId)
                .toList();
    }

    /**
     * 校验文件的正式文件ID是不是没有被引用
     */
    private boolean isUnused(BoxUserFile boxUserFile) {
        BoxUserFileQuery boxUserFileQuery = new BoxUserFileQuery();
        boxUserFileQuery.setRealFileId(boxUserFile.getRealFileId());
        Integer count = boxUserFileService.selectCountByParam(boxUserFileQuery);
        return Objects.equals(count, Constants.ZERO_INT);
    }
}
