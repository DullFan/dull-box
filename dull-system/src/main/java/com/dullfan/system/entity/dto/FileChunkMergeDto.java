package com.dullfan.system.entity.dto;

import com.dullfan.system.entity.po.BoxFile;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class FileChunkMergeDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -34864285976679341L;

    /**
     * 文件唯一标识
     */
    private String filename;

    /**
     * 文件唯一标识
     */
    private String identifier;

    /**
     * 文件总大小
     */
    private Long totalSize;

    /**
     * 文件的父文件夹ID
     */
    private Long parentId;

    /**
     * 物理文件记录
     */
    private BoxFile record;

    /**
     * 文件合并之后存储物理路径
     */
    private String realPath;
}
