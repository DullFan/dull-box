package com.dullfan.system.entity.po;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;

@Data
public class FileUploadPO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3300832238613686432L;

    @ApiModelProperty(value = "文件的唯一标识",required = true)
    @NotBlank(message = "文件的唯一标识不能为空")
    private String identifier;

    @ApiModelProperty(value = "文件的父文件夹ID",required = true)
    @NotBlank(message = "文件的父文件夹ID不能为空")
    private String parentId;

    @ApiModelProperty(value = "文件实体",required = true)
    @NotNull(message = "文件实体不能为空")
    private MultipartFile file;
}
