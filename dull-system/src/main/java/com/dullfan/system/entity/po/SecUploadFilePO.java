package com.dullfan.system.entity.po;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class SecUploadFilePO implements Serializable {

    private static final Long serializableVersionUID = -1231231234454L;

    @ApiModelProperty(value = "文件夹ID",required = true)
    @NotBlank(message = "文件夹ID不能为空")
    private String parentId;

    @ApiModelProperty(value = "文件名称",required = true)
    @NotBlank(message = "文件名称不能为空")
    private String filename;

    @ApiModelProperty(value = "文件的唯一标识",required = true)
    @NotBlank(message = "文件的唯一标识不能为空")
    private String identifier;
}
