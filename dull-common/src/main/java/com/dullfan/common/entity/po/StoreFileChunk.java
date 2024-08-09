package com.dullfan.common.entity.po;

import lombok.Data;

import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;

/**
 * 保存文件分片的上下文信息
 */
@Data
public class StoreFileChunk implements Serializable {
    @Serial
    private static final long serialVersionUID = -3132644711545307248L;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件唯一标识
     */
    private String identifier;

    /**
     * 文件的总大小
     */
    private Long totalSize;

    /**
     * 文件输入流
     */
    private InputStream inputStream;

    /**
     * 文件存储真实存储路径
     */
    private String realPath;

    /**
     * 文件总分片数
     */
    private Integer totalChunks;

    /**
     * 当前分片的下标
     */
    private Integer chunkNumber;

    /**
     * 当前分片的大小
     */
    private Long currentChunkSize;

    /**
     * 用户ID
     */
    private Long userId;
}
