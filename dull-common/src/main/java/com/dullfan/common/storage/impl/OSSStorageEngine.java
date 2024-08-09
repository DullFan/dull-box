package com.dullfan.common.storage.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.ServiceException;
import com.aliyun.oss.model.*;
import com.dullfan.common.constant.Constants;
import com.dullfan.common.entity.po.DeleteFile;
import com.dullfan.common.entity.po.MergeFile;
import com.dullfan.common.entity.po.StoreFile;
import com.dullfan.common.entity.po.StoreFileChunk;
import com.dullfan.common.enums.ReadFile;
import com.dullfan.common.storage.AbstractStorageEngine;
import com.dullfan.common.storage.oss.OssStorageEngineConfig;
import com.dullfan.common.utils.StringUtils;
import com.dullfan.common.utils.encryption.FileUtils;
import com.dullfan.common.utils.uuid.IdUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.rmi.ServerException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基于OSS的文件存储引擎实现
 */
@Component("OSSStorageEngine")
public class OSSStorageEngine extends AbstractStorageEngine {
    private static final Integer TEN_THOUSAND_INT = 10000;
    private static final String CACHE_KEY_TEMPLATE = "oss_cache_upload_id_{}_{}s";

    private static final String IDENTIFIER_KEY = "identifier";

    private static final String UPLOAD_ID_KEY = "uploadId";

    private static final String USER_ID_KEY = "userId";

    private static final String PART_NUMBER_KEY = "partNumber";

    private static final String E_TAG_KEY = "eTag";

    private static final String PART_SIZE_KEY = "partSize";

    private static final String PART_CRC_KEY = "partCRC";

    @Resource
    private OSS oss;

    @Resource
    private OssStorageEngineConfig config;

    @Override
    protected void doStore(StoreFile storeFile) throws IOException {
        String realPath = getFilePath(FileUtils.getFileSuffix(storeFile.getFilename()));
        oss.putObject(config.getBucketName(), realPath, storeFile.getInputStream());
        storeFile.setRealPath(realPath);
    }


    @Override
    protected void doDelete(DeleteFile deleteFile) throws IOException {
        List<String> realFilePathList = deleteFile.getRealFilePathList();
        realFilePathList.forEach(realPath -> {
            if(checkHaveParams(realPath)){
                // 文件分片存储路径
                JSONObject params = analysisUrlParams(realPath);
                if(!params.isEmpty()){
                    String uploadId = params.getString(UPLOAD_ID_KEY);
                    String identifier = params.getString(IDENTIFIER_KEY);
                    Long userId = params.getLong(USER_ID_KEY);
                    String cacheKey = getCacheKey(identifier, userId);
                    getCache().evict(cacheKey);
                    try {
                        AbortMultipartUploadRequest request = new AbortMultipartUploadRequest(config.getBucketName(), getBaseUrl(realPath), uploadId);
                        oss.abortBucketWorm(request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // 普通文件的物理删除
                oss.deleteObject(config.getBucketName(),realPath);
            }
        });
    }


    @Override
    protected synchronized void doStoreChunk(StoreFileChunk storeFileChunk) throws IOException {
        if (storeFileChunk.getTotalChunks() > TEN_THOUSAND_INT) {
            throw new ServerException("分片数超过了限制，分片数不得大于： " + TEN_THOUSAND_INT);
        }

        String cacheKey = getCacheKey(storeFileChunk.getIdentifier(), storeFileChunk.getUserId());

        ChunkUploadEntity entity = getCache().get(cacheKey, ChunkUploadEntity.class);

        if (Objects.isNull(entity)) {
            entity = initChunkUpload(storeFileChunk.getFilename(), cacheKey);
        }

        UploadPartRequest request = new UploadPartRequest();
        request.setBucketName(config.getBucketName());
        request.setKey(entity.getObjectKey());
        request.setUploadId(entity.getUploadId());
        request.setInputStream(storeFileChunk.getInputStream());
        request.setPartSize(storeFileChunk.getCurrentChunkSize());
        request.setPartNumber(storeFileChunk.getChunkNumber());

        UploadPartResult result = oss.uploadPart(request);

        if (Objects.isNull(result)) {
            throw new ServerException("文件分片上传失败");
        }

        PartETag partETag = result.getPartETag();

        // 拼装文件分片的url
        JSONObject params = new JSONObject();
        params.put(IDENTIFIER_KEY, storeFileChunk.getIdentifier());
        params.put(UPLOAD_ID_KEY, entity.getUploadId());
        params.put(USER_ID_KEY, storeFileChunk.getUserId());
        params.put(PART_NUMBER_KEY, partETag.getPartNumber());
        params.put(E_TAG_KEY, partETag.getETag());
        params.put(PART_SIZE_KEY, partETag.getPartSize());
        params.put(PART_CRC_KEY, partETag.getPartCRC());

        String realPath = assembleUrl(entity.getObjectKey(), params);

        storeFileChunk.setRealPath(realPath);
    }

    @Override
    protected void doMergeFile(MergeFile mergeFile) throws IOException {

        String cacheKey = getCacheKey(mergeFile.getIdentifier(), mergeFile.getUserId());

        ChunkUploadEntity entity = getCache().get(cacheKey, ChunkUploadEntity.class);

        if (Objects.isNull(entity)) {
            throw new ServerException("文件分片合并失败，文件的唯一标识为：" + mergeFile.getIdentifier());
        }

        List<String> chunkPaths = mergeFile.getRealPathList();
        List<PartETag> partETags = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(chunkPaths)) {
            partETags = chunkPaths.stream()
                    .filter(org.apache.commons.lang3.StringUtils::isNotBlank)
                    .map(this::analysisUrlParams)
                    .filter(jsonObject -> !jsonObject.isEmpty())
                    .map(jsonObject -> new PartETag(jsonObject.getIntValue(PART_NUMBER_KEY),
                            jsonObject.getString(E_TAG_KEY),
                            jsonObject.getLongValue(PART_SIZE_KEY),
                            jsonObject.getLong(PART_CRC_KEY)
                    )).collect(Collectors.toList());
        }

        CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(config.getBucketName(), entity.getObjectKey(), entity.uploadId, partETags);
        CompleteMultipartUploadResult result = oss.completeMultipartUpload(request);
        if (Objects.isNull(result)) {
            throw new ServerException("文件分片合并失败，文件的唯一标识为：" + mergeFile.getIdentifier());
        }

        getCache().evict(cacheKey);

        mergeFile.setRealPath(entity.getObjectKey());
        mergeFile.setTotalSize(mergeFile.getTotalSize());
    }

    @Override
    protected void doReadFile(ReadFile readFile) throws IOException {
        OSSObject object = oss.getObject(config.getBucketName(), readFile.getRealPath());
        if(Objects.isNull(object)){
            throw new ServerException("文件读取失败,文件的名称为:"+readFile.getRealPath());
        }
        FileUtils.writeStream2StreamNormal(object.getObjectContent(),readFile.getOutputStream());
    }

    /**
     * 获取对象的完整名称
     * 生成规则：年 + 月 + 日 + 随机的文件名称
     */
    private String getFilePath(String fileSuffix) {
        return DateUtil.thisYear() + File.separator + (DateUtil.thisMonth() + 1) + File.separator + DateUtil.thisDayOfMonth() + File.separator + IdUtils.fastUUID() + fileSuffix;
    }

    /**
     * 获取分片上传的缓存key
     */
    private String getCacheKey(String identifier, Long userId) {
        return StringUtils.format(CACHE_KEY_TEMPLATE, identifier, userId);
    }

    /**
     * 初始化文件分片上传
     */
    private ChunkUploadEntity initChunkUpload(String filename, String cacheKey) {
        String filePath = getFilePath(filename);
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(config.getBucketName(), filePath);
        InitiateMultipartUploadResult result = oss.initiateMultipartUpload(request);
        if (Objects.isNull(result)) {
            throw new ServiceException("文件分片上传初始化失败");
        }
        ChunkUploadEntity chunkUploadEntity = new ChunkUploadEntity();
        chunkUploadEntity.setObjectKey(filePath);
        chunkUploadEntity.setUploadId(result.getUploadId());
        getCache().put(cacheKey, chunkUploadEntity);
        return chunkUploadEntity;
    }

    /**
     * 拼装URL
     * @return baseUrl?paramKey1=paramValue1&paramKey2=paramValue2
     */
    private String assembleUrl(String baseUrl, JSONObject params) {
        if (Objects.isNull(params) || params.isEmpty()) {
            return baseUrl;
        }
        StringBuilder urlStringBuffer = new StringBuilder(baseUrl);
        urlStringBuffer.append(Constants.QUESTION_MARK_STR);
        List<String> paramsList = Lists.newArrayList();
        StringBuffer urlParamsStringBuffer = new StringBuffer();
        params.forEach((key, value) -> {
            urlParamsStringBuffer.setLength(Constants.ZERO_INT);
            urlParamsStringBuffer.append(key);
            urlParamsStringBuffer.append(Constants.EQUALS_MARK_STR);
            urlParamsStringBuffer.append(value);
            paramsList.add(urlParamsStringBuffer.toString());
        });
        return urlStringBuffer.append(Joiner.on(Constants.AND_MARK_STR).join(paramsList)).toString();
    }

    /**
     * 获取基础URL
     */
    private String getBaseUrl(String url) {
        if (org.apache.commons.lang3.StringUtils.isBlank(url)) {
            return Constants.EMPTY_STR;
        }
        if (checkHaveParams(url)) {
            return url.split(getSplitMark(Constants.QUESTION_MARK_STR))[0];
        }
        return url;
    }

    /**
     * 获取截取字符串的关键标识
     * 由于java的字符串分割会按照正则去截取
     * 我们的URL会影响标识的识别，故添加左右中括号去分组
     */
    private String getSplitMark(String mark) {
        return Constants.LEFT_BRACKET_STR +
                mark +
                Constants.RIGHT_BRACKET_STR;
    }

    /**
     * 分析URL参数
     */
    private JSONObject analysisUrlParams(String url) {
        JSONObject result = new JSONObject();
        if (!checkHaveParams(url)) {
            return result;
        }
        String paramsPart = url.split(getSplitMark(Constants.QUESTION_MARK_STR))[1];
        if (org.apache.commons.lang3.StringUtils.isNotBlank(paramsPart)) {
            List<String> paramPairList = Splitter.on(Constants.AND_MARK_STR).splitToList(paramsPart);
            paramPairList.forEach(paramPair -> {
                String[] paramArr = paramPair.split(getSplitMark(Constants.EQUALS_MARK_STR));
                if (paramArr.length == Constants.TWO_INT) {
                    result.put(paramArr[0], paramArr[1]);
                }
            });
        }
        return result;
    }

    /**
     * 检查是否是含有参数的URL
     */
    private boolean checkHaveParams(String url) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(url) && url.indexOf(Constants.QUESTION_MARK_STR) != Constants.MINUS_ONE_INT;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    public static class ChunkUploadEntity implements Serializable {

        @Serial
        private static final long serialVersionUID = 5248054767630903719L;

        /**
         * 分片上传全局唯一的upload
         */
        private String uploadId;

        /**
         * 文件分片上传的实体名称
         */
        private String objectKey;
    }
}
