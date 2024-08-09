package com.dullfan.common.storage;

import com.dullfan.common.entity.po.DeleteFile;
import com.dullfan.common.entity.po.MergeFile;
import com.dullfan.common.entity.po.StoreFile;
import com.dullfan.common.entity.po.StoreFileChunk;
import com.dullfan.common.enums.ReadFile;

import java.io.IOException;

/**
 * 文件存储引擎的顶级接口
 */
public interface StorageEngine {

    /**
     * 存储物理文件
     */
    void store(StoreFile storeFile) throws IOException;

    /**
     * 删除物理文件
     */
    void delete(DeleteFile deleteFile) throws IOException;

    /**
     * 文件分片上传
     */
    void storeChunk(StoreFileChunk storeFileChunk)throws IOException;

    /**
     * 合并文件分片
     */
    void mergeFile(MergeFile mergeFile) throws IOException;

    /**
     * 读取文件内容写入到输出流中
     */
    void realFile(ReadFile readFile) throws IOException;
}
