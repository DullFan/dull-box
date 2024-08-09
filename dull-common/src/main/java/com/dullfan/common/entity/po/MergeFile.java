package com.dullfan.common.entity.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 合并文件对象
 */
@Data
public class MergeFile implements Serializable {
    @Serial
    private static final long serialVersionUID = 7429992821232361465L;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件唯一标识
     */
    private String identifier;

    /**
     * 当前登录用户的ID
     */
    private Long userId;
    /**
     * 文件分片的真实存储路径集合
     */
    private List<String> realPathList;

    /**
     * 文件合并后的真实物理存储路径
     */
    private String realPath;

    /**
     * 文件的总大小
     */
    private Long totalSize;

}
