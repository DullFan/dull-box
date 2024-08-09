package com.dullfan.common.storage.impl;

import com.dullfan.common.constant.Constants;
import com.dullfan.common.entity.po.DeleteFile;
import com.dullfan.common.entity.po.MergeFile;
import com.dullfan.common.entity.po.StoreFile;
import com.dullfan.common.entity.po.StoreFileChunk;
import com.dullfan.common.enums.ReadFile;
import com.dullfan.common.storage.AbstractStorageEngine;
import com.dullfan.common.storage.fastdfs.FastDFSStorageEngineConfig;
import com.dullfan.common.utils.encryption.FileUtils;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.ServerException;
import java.util.List;

/**
 * 基于FastDFS实现的文件存储引擎
 */
@Component("FastDFSStorageEngine")
public class FastDFSStorageEngine extends AbstractStorageEngine {

    @Resource
    protected FastFileStorageClient client;

    @Resource
    FastDFSStorageEngineConfig config;


    @Override
    protected void doStore(StoreFile storeFile) throws IOException {
        StorePath storePath = client.uploadFile(
                config.getGroup(),
                storeFile.getInputStream(),
                storeFile.getTotalSize(),
                FileUtils.getFileExtName(storeFile.getFilename()));
        storeFile.setRealPath(storePath.getFullPath());
    }

    @Override
    protected void doDelete(DeleteFile deleteFile) throws IOException {
        List<String> realFilePathList = deleteFile.getRealFilePathList();
        if(CollectionUtils.isNotEmpty(realFilePathList)){
            realFilePathList.forEach(client::deleteFile);
        }
    }

    @Override
    protected void doStoreChunk(StoreFileChunk storeFileChunk) throws IOException {
        throw new ServerException("不支持分片上传");
    }

    @Override
    protected void doMergeFile(MergeFile mergeFile) throws IOException {
        throw new ServerException("不支持分片上传");
    }

    @Override
    protected void doReadFile(ReadFile readFile) throws IOException {
        String realPath = readFile.getRealPath();
        String group = realPath.substring(Constants.ZERO_INT, realPath.indexOf(Constants.SLASH_STR));
        String path = realPath.substring(realPath.indexOf(Constants.SLASH_STR)+Constants.ONE_INT);
        DownloadByteArray downloadByteArray = new DownloadByteArray();
        byte[] bytes = client.downloadFile(group, path, downloadByteArray);
        OutputStream outputStream = readFile.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
