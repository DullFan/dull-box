package com.dullfan.system.service.impl;

import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.common.enums.DelFlagEnum;
import com.dullfan.common.exception.ServiceException;
import com.dullfan.common.storage.StorageEngine;
import com.dullfan.common.utils.SecurityUtils;
import com.dullfan.common.utils.encryption.IdUtil;
import com.dullfan.system.entity.dto.DeleteDto;
import com.dullfan.system.entity.po.BoxUserFile;
import com.dullfan.system.entity.po.DeleteFilePO;
import com.dullfan.system.entity.query.BoxUserFileQuery;
import com.dullfan.system.entity.vo.BoxUserFileVO;
import com.dullfan.system.event.file.FileDeleteEvent;
import com.dullfan.system.service.BoxUserFileService;
import com.dullfan.system.service.RecycleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.keyvalue.core.event.KeyValueEvent.afterDelete;

@Slf4j
@Service("RecycleServiceImpl")
public class RecycleServiceImpl implements RecycleService {
    @Resource
    private BoxUserFileService boxUserFileService;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 查询回收站列表
     */
    @Override
    public List<BoxUserFile> recycles() {
        BoxUserFileQuery boxUserFileQuery = new BoxUserFileQuery();
        boxUserFileQuery.setUserId(SecurityUtils.getUserId());
        boxUserFileQuery.setDelFlag(DelFlagEnum.YES.getCode());
        return boxUserFileService.recycles();
    }

    /**
     * 文件彻底删除
     */
    @Override
    public void delete(DeleteFilePO deleteFilePO) {
        DeleteDto deleteDto = new DeleteDto();
        deleteDto.setUserId(deleteFilePO.getUserId());
        deleteDto.setFileIdList(deleteFilePO.getFileIds().stream().map(IdUtil::decrypt).toList());
        checkFileDelete(deleteDto);
        findAllFileRecords(deleteDto);
        doDelete(deleteDto);
        afterDelete(deleteDto);
    }

    /**
     * 删除文件后置操作
     */
    private void afterDelete(DeleteDto deleteDto) {
        FileDeleteEvent fileDeleteEvent = new FileDeleteEvent(this, deleteDto.getAllRecords());
        applicationContext.publishEvent(fileDeleteEvent);
    }

    /**
     * 执行文件删除动作
     */
    private void doDelete(DeleteDto deleteDto) {
        List<Long> fileIdList = deleteDto.getAllRecords().stream().map(BoxUserFile::getFileId).toList();
        if(boxUserFileService.deleteByFileIds(fileIdList) == 0){
            throw new ServiceException("文件记录删除失败");
        }
    }

    /**
     * 递归查询所有子文件
     */
    private void findAllFileRecords(DeleteDto deleteDto) {
        List<BoxUserFile> records = deleteDto.getRecords();
        List<BoxUserFile> allRecords = boxUserFileService.findAllFileRecords(records);
        deleteDto.setAllRecords(allRecords);
    }

    /**
     * 验证文件删除的操作权限
     */
    private void checkFileDelete(DeleteDto deleteDto) {
        List<BoxUserFile> boxUserFiles = boxUserFileService.selectByFileIds(deleteDto.getFileIdList(),deleteDto.getUserId());
        if(CollectionUtils.isEmpty(boxUserFiles) || boxUserFiles.size() != deleteDto.getFileIdList().size()){
            throw new ServiceException("无法删除该文件");
        }
        deleteDto.setRecords(boxUserFiles);
    }
}
