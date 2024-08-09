package com.dullfan.system.service;

import com.dullfan.system.entity.dto.FileChunkUploadDto;
import com.dullfan.system.entity.po.BoxFileChunk;
import com.dullfan.system.entity.query.BoxFileChunkQuery;
import com.dullfan.system.entity.vo.UploadedChunksVO;

import java.util.List;

public interface FileChunkService {
    /**
     * 文件分片保存
     */
    void saveChunkFile(FileChunkUploadDto fileChunkUploadDto);

    /**
     * 查询已上传的分片列表
     */
    UploadedChunksVO checkUploadChunks(String identifier);

    /**
     * 查询用户文件分片信息列表
     */
    List<BoxFileChunk> selectList(BoxFileChunkQuery boxFileChunkQuery);


    /**
     *  批量删除分片信息
     */
    void removeByIds(List<Long> fileChunkRecordIdList);

}
