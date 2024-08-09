package com.dullfan.system.entity.po;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class QueryUploadedChunksPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3605579030365086128L;


    @ApiModelProperty(value = "文件的唯一标识",required = true)
    @NotBlank(message = "文件唯一标识不能为空")
    private String identifier;

}
