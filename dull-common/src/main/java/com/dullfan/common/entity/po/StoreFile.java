package com.dullfan.common.entity.po;

import lombok.Data;

import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;

/**
 * 文件存储引擎物理文件实体
 */
@Data
public class StoreFile implements Serializable {
    @Serial
    private static final long serialVersionUID = -585023175567137833L;

    /**
     * 上传的文件名称
     */
    private String filename;

    /**
     * 文件的总大小
     */
    private Long totalSize;

    /**
     * 文件的输入流信息
     */
    private InputStream inputStream;

    /**
     * 文件上传后的物理路径
     */
    private String realPath;
}
