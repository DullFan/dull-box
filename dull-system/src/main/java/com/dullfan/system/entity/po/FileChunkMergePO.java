package com.dullfan.system.entity.po;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class FileChunkMergePO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6419156893022171216L;

    @ApiModelProperty(value = "文件名称",required = true)
    @NotBlank(message = "文件名称不能为空")
    private String filename;

    @ApiModelProperty(value = "文件的唯一标识",required = true)
    @NotBlank(message = "文件的唯一标识不能为空")
    private String identifier;

    @ApiModelProperty(value = "文件的父文件夹ID",required = true)
    @NotBlank(message = "文件的父文件夹ID不能为空")
    private String parentId;

    @ApiModelProperty(value = "文件的总大小",required = true)
    @NotNull(message = "文件的总大小不能为空")
    private Long totalSize;
}
