package com.dullfan.common.storage;

import cn.hutool.core.lang.Assert;
import com.dullfan.common.constant.CacheConstants;
import com.dullfan.common.entity.po.DeleteFile;
import com.dullfan.common.entity.po.MergeFile;
import com.dullfan.common.entity.po.StoreFile;
import com.dullfan.common.entity.po.StoreFileChunk;
import com.dullfan.common.enums.ReadFile;
import com.dullfan.common.exception.ServiceException;
import jakarta.annotation.Resource;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.io.IOException;
import java.util.Objects;

/**
 * 顶级文件存储引擎的公用父类
 */
public abstract class AbstractStorageEngine implements StorageEngine{

    @Resource
    private CacheManager cacheManager;


    /**
     * 公用的获取缓存的方法
     */
    protected Cache getCache(){
        if(Objects.isNull(cacheManager)){
            throw new ServiceException("缓存管理器为空");
        }
        return cacheManager.getCache(CacheConstants.BOX_CACHE_NAME);
    }

    /**
     * 存储物理文件
     */
    @Override
    public void store(StoreFile storeFile) throws IOException {
        checkStoreFile(storeFile);
        doStore(storeFile);
    }

    /**
     * 执行保存物理文件的动作
     */
    protected abstract void doStore(StoreFile storeFile) throws IOException;

    /**
     * 校验上传物理文件信息
     */
    private void checkStoreFile(StoreFile storeFile) {
        Assert.notBlank(storeFile.getFilename(),"文件名称不能为空");
        Assert.notNull(storeFile.getTotalSize(),"文件的总大小不能为空");
        Assert.notNull(storeFile.getInputStream(),"文件不能为空");
    }

    /**
     * 删除物理文件
     */
    @Override
    public void delete(DeleteFile deleteFile) throws IOException {
        checkDeleteFile(deleteFile);
        doDelete(deleteFile);
    }

    /**
     * 执行删除物理文件的动作
     */
    protected abstract void doDelete(DeleteFile deleteFile) throws IOException;

    /**
     * 校验删除物理信息
     */
    private void checkDeleteFile(DeleteFile deleteFile) {
        Assert.notEmpty(deleteFile.getRealFilePathList(),"要删除的文件路径不能为空");
    }

    @Override
    public void storeChunk(StoreFileChunk storeFileChunk) throws IOException {
        checkStoreFileChunk(storeFileChunk);
        doStoreChunk(storeFileChunk);
    }

    private void checkStoreFileChunk(StoreFileChunk storeFileChunk) {
        Assert.notBlank(storeFileChunk.getFilename(),"文件名称不能为空");
        Assert.notBlank(storeFileChunk.getIdentifier(),"文件唯一标识不能为空");
        Assert.notNull(storeFileChunk.getInputStream(),"文件分片不能为空");
        Assert.notNull(storeFileChunk.getTotalChunks(),"文件分片总数不能为空");
        Assert.notNull(storeFileChunk.getChunkNumber(),"文件分片下标不能为空");
        Assert.notNull(storeFileChunk.getCurrentChunkSize(),"文件分片的大小不能为空");
        Assert.notNull(storeFileChunk.getUserId(),"用户ID不能为空");
    }

    /**
     * 执行分片上传
     */
    protected abstract void doStoreChunk(StoreFileChunk storeFileChunk) throws IOException;

    @Override
    public void mergeFile(MergeFile mergeFile) throws IOException {
        checkMergeFile(mergeFile);
        doMergeFile(mergeFile);
    }

    /**
     * 检查参数是否为空
     */
    private void checkMergeFile(MergeFile mergeFile) {
        Assert.notBlank(mergeFile.getFilename(),"文件名称不能为空");
        Assert.notBlank(mergeFile.getIdentifier(),"文件唯一标识不能为空");
        Assert.notNull(mergeFile.getUserId(),"用户ID不能为空");
        Assert.notEmpty(mergeFile.getRealPathList(),"文件分片的真实存储路径集合不能为空");
    }


    /**
     * 执行文件分片合并
     */
    protected abstract void doMergeFile(MergeFile mergeFile) throws IOException;

    /**
     * 读取文件内容并写入到输出流中
     * @param readFile
     * @throws IOException
     */
    @Override
    public void realFile(ReadFile readFile) throws IOException {
        checkReadFile(readFile);
        doReadFile(readFile);
    }

    /**
     * 文件读取参数校验
     */
    private void checkReadFile(ReadFile readFile) {
        Assert.notBlank(readFile.getRealPath(),"文件真实存储路径不能为空");
        Assert.notNull(readFile.getOutputStream(),"文件的输出流不能为空");

    }


    /**
     * 执行读取文件内容并写入到输出流中
     */
    protected abstract void doReadFile(ReadFile readFile) throws IOException;

}
