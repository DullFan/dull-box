package com.dullfan.system.entity.po;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;

@Data
public class FileChunkUploadPO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4996273449167579855L;

    @ApiModelProperty(value = "文件唯一标识",required = true)
    @NotBlank(message = "文件唯一标识")
    private String identifier;

    @ApiModelProperty(value = "文件名称",required = true)
    @NotBlank(message = "文件名称不能为空")
    private String filename;

    @ApiModelProperty(value = "总体的分片数",required = true)
    @NotNull(message = "总体的分片数不能为空")
    private Integer totalChunks;

    @ApiModelProperty(value = "当前分片下标",required = true)
    @NotNull(message = "当前分片下标不能为空")
    private Integer chunkNumber;

    @ApiModelProperty(value = "文件总大小",required = true)
    @NotNull(message = "文件总大小不能为空")
    private Integer totalSize;

    @ApiModelProperty(value = "实体文件",required = true)
    @NotNull(message = "实体文件不能为空")
    private MultipartFile file;
}
