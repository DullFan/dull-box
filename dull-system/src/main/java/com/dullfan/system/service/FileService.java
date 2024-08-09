package com.dullfan.system.service;

import com.dullfan.system.entity.dto.BoxUserFileDto;
import com.dullfan.system.entity.dto.CopyFileDto;
import com.dullfan.system.entity.dto.FileSearchDto;
import com.dullfan.system.entity.dto.TransferFileDto;
import com.dullfan.system.entity.po.*;
import com.dullfan.system.entity.vo.*;

import java.util.List;

public interface FileService {

    /**
     * 新增
     */
    Long insert(BoxUserFile bean);

    /**
     * 查询文件列表
     */
    List<BoxUserFile> selectList(BoxUserFileDto boxUserFileDto);

    /**
     * 创建文件夹
     */
    Long createFolder(CreateFolderPO createFolderPO);

    /**
     * 更新文件名称
     */
    Integer updateFilename(UpdateFilenamePO updateFilenamePO);

    /**
     * 文件秒传
     */
    boolean secUpload(SecUploadFilePO secUploadFilePO);

    /**
     * 单文件上传
     */
    void upload(FileUploadPO fileUploadPO);

    /**
     * 文件分片上传
     */
    FileChunkUploadVO chunkUpload(FileChunkUploadPO fileChunkUploadPO);

    /**
     * 查询用户已上传的文件列表
     */
    UploadedChunksVO getUploadedChunks(QueryUploadedChunksPO queryUploadedChunksPO);

    /**
     * 文件分片合并
     */
    void mergeFile(FileChunkMergePO fileChunkMergePO);

    /**
     * 文件下载
     */
    void download(FileDownload fileDownload);

    /**
     * 文件下载,不检查用户权限
     */
    void downloadWithoutCheckUser(FileDownload fileDownload);

    /**
     * 文件预览
     */
    void preview(FilePreview filePreview);
    /**
     * 文件预览,不检查用户权限
     */
    void previewWithoutCheckUser(FilePreview filePreview);

    /**
     * 获取Box树形结构
      */
    List<FolderTreeNodeVO> getFolderTree();

    /**
     * 文件转移操作
     */
    void transfer(TransferFileDto transferFileDto);

    /**
     * 文件复制操作
     */
    void copy(CopyFileDto copyFileDto);

    /**
     * 文件列表搜索
     */
    List<BoxUserFileVO> search(FileSearchDto fileSearchDto);

    /**
     * 获取面包屑列表
     */
    List<BreadcrumbVO> getBreadcrumbs(Long fileId);
}
