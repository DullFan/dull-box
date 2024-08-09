package com.dullfan.system.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class FileChunkUploadVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1918182624712432660L;

    @ApiModelProperty("是否需要合并文件 0 不需要 1 需要")
    private Integer mergeFlag;
}
