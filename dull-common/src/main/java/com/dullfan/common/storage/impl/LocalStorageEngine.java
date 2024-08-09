package com.dullfan.common.storage.impl;

import com.dullfan.common.config.DullConfig;
import com.dullfan.common.entity.po.DeleteFile;
import com.dullfan.common.entity.po.MergeFile;
import com.dullfan.common.entity.po.StoreFile;
import com.dullfan.common.entity.po.StoreFileChunk;
import com.dullfan.common.enums.ReadFile;
import com.dullfan.common.storage.AbstractStorageEngine;
import com.dullfan.common.utils.encryption.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * 本地文件存储引擎实现类
 */
@Component("LocalStorageEngine")
public class LocalStorageEngine extends AbstractStorageEngine {
    @Override
    protected void doStore(StoreFile storeFile) throws IOException {
        String basePath = DullConfig.getUploadPath();
        String realFilePath = FileUtils.generateStoreFileRealPath(basePath, storeFile.getFilename());
        FileUtils.writeStream2File(storeFile.getInputStream(),new File(realFilePath),storeFile.getTotalSize());
        storeFile.setRealPath(realFilePath);
    }

    @Override
    protected void doDelete(DeleteFile deleteFile) throws IOException {
        FileUtils.deleteFiles(deleteFile.getRealFilePathList());
    }

    @Override
    protected void doStoreChunk(StoreFileChunk storeFileChunk) throws IOException {
        String basePath = DullConfig.getUploadStoreChunkPath();
        String realFilePath = FileUtils.generateStoreFileChunkRealPath(basePath, storeFileChunk.getIdentifier(),storeFileChunk.getChunkNumber());
        FileUtils.writeStream2File(storeFileChunk.getInputStream(),new File(realFilePath),storeFileChunk.getCurrentChunkSize());
        storeFileChunk.setRealPath(realFilePath);
    }

    @Override
    protected void doMergeFile(MergeFile mergeFile) throws IOException {
        String basePath = DullConfig.getUploadPath();
        String realFilePath = FileUtils.generateStoreFileRealPath(basePath, mergeFile.getFilename());
        FileUtils.createFile(new File(realFilePath));
        List<String> chunkPaths = mergeFile.getRealPathList();
        long totalSize = 0L;
        for (String chunkPath : chunkPaths) {
            File file = new File(chunkPath);
            FileUtils.appendWrite(Paths.get(realFilePath),file.toPath());
            totalSize += file.length();
        }

        FileUtils.deleteFiles(chunkPaths);
        mergeFile.setRealPath(realFilePath);
        mergeFile.setTotalSize(totalSize);
    }

    @Override
    protected void doReadFile(ReadFile readFile) throws IOException {
        File file = new File(readFile.getRealPath());
        FileUtils.writeFile2OutputStream(new FileInputStream(file),readFile.getOutputStream(),file.length());





    }
}
