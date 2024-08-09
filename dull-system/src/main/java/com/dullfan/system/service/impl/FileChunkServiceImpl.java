package com.dullfan.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.dullfan.common.config.BoxServiceConfig;
import com.dullfan.common.entity.po.StoreFileChunk;
import com.dullfan.common.enums.MergeFlagEnum;
import com.dullfan.common.exception.ServiceException;
import com.dullfan.common.storage.StorageEngine;
import com.dullfan.common.utils.DateUtils;
import com.dullfan.common.utils.SecurityUtils;
import com.dullfan.common.utils.encryption.IdUtil;
import com.dullfan.system.entity.dto.FileChunkUploadDto;
import com.dullfan.system.entity.po.BoxFileChunk;
import com.dullfan.system.entity.query.BoxFileChunkQuery;
import com.dullfan.system.entity.vo.UploadedChunksVO;
import com.dullfan.system.service.BoxFileChunkService;
import com.dullfan.system.service.FileChunkService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class FileChunkServiceImpl implements FileChunkService {

    @Resource
    BoxFileChunkService boxFileChunkService;

    @Resource
    BoxServiceConfig boxServiceConfig;

    @Resource
    StorageEngine storageEngine;

    /**
     * 文件分片保存
     */
    @Override
    public synchronized void saveChunkFile(FileChunkUploadDto fileChunkUploadDto) {
        doSaveChunkFile(fileChunkUploadDto);
        doJudgeMergeFile(fileChunkUploadDto);
    }

    @Override
    public UploadedChunksVO checkUploadChunks(String identifier) {
        BoxFileChunkQuery boxFileChunkQuery = new BoxFileChunkQuery();
        boxFileChunkQuery.setIdentifier(identifier);
        boxFileChunkQuery.setCreateUser(SecurityUtils.getUserId());
        boxFileChunkQuery.setExpirationTimeStart(new Date());
        List<BoxFileChunk> boxFileChunks = boxFileChunkService.selectListByParamNoPage(boxFileChunkQuery);
        List<Integer> list = boxFileChunks.stream().map(BoxFileChunk::getChunkNumber).toList();
        UploadedChunksVO uploadedChunksVo = new UploadedChunksVO();
        uploadedChunksVo.setUploadedChunks(list);
        return uploadedChunksVo;
    }

    @Override
    public List<BoxFileChunk> selectList(BoxFileChunkQuery boxFileChunkQuery) {
        return boxFileChunkService.selectListByParamNoPage(boxFileChunkQuery);
    }

    @Override
    public void removeByIds(List<Long> fileChunkRecordIdList) {
        boxFileChunkService.deleteByIdBatch(fileChunkRecordIdList);
    }

    /**
     * 判断是否所有的分片上传完成
     */
    private void doJudgeMergeFile(FileChunkUploadDto fileChunkUploadDto) {
        BoxFileChunkQuery boxFileChunkQuery = new BoxFileChunkQuery();
        boxFileChunkQuery.setIdentifier(fileChunkUploadDto.getIdentifier());
        boxFileChunkQuery.setCreateUser(fileChunkUploadDto.getUserId());
        Integer count = boxFileChunkService.selectCountByParam(boxFileChunkQuery);
        if (count == fileChunkUploadDto.getTotalChunks().intValue()) {
            fileChunkUploadDto.setMergeFlagEnum(MergeFlagEnum.READY);
        }
    }

    /**
     * 执行文件分片上传保存操作
     */
    private void doSaveChunkFile(FileChunkUploadDto fileChunkUploadDto) {
        doStoreFileChunk(fileChunkUploadDto);
        doSaveRecord(fileChunkUploadDto);
    }

    /**
     * 保存文件分片记录
     */
    private void doSaveRecord(FileChunkUploadDto fileChunkUploadDto) {
        BoxFileChunk boxFileChunk = new BoxFileChunk();
        boxFileChunk.setId(IdUtil.get());
        boxFileChunk.setIdentifier(fileChunkUploadDto.getIdentifier());
        boxFileChunk.setRealPath(fileChunkUploadDto.getRealPath());
        boxFileChunk.setChunkNumber(fileChunkUploadDto.getChunkNumber());
        boxFileChunk.setExpirationTime(DateUtil.offsetDay(DateUtils.getNowDate(), boxServiceConfig.getChunkFileExpirationDays()));
        boxFileChunk.setCreateTime(DateUtils.getNowDate());
        boxFileChunk.setCreateUser(SecurityUtils.getUserId());
        if (!(boxFileChunkService.insert(boxFileChunk) >= 1)) {
            throw new ServiceException("文件分片上传失败");
        }
    }

    /**
     * 委托文件存储引擎保存文件分片
     */
    private void doStoreFileChunk(FileChunkUploadDto fileChunkUploadDto) {
        try {
            StoreFileChunk storeFileChunk = new StoreFileChunk();
            BeanUtils.copyProperties(fileChunkUploadDto, storeFileChunk);
            storeFileChunk.setInputStream(fileChunkUploadDto.getFile().getInputStream());
            storeFileChunk.setUserId(SecurityUtils.getUserId());
            storageEngine.storeChunk(storeFileChunk);
            fileChunkUploadDto.setRealPath(storeFileChunk.getRealPath());
        } catch (IOException e) {
            throw new ServiceException("文件分片上传失败");
        }
    }
}
