package com.dullfan.system.entity.po;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件搜索参数实体
 */
@Data
public class FileSearchPO implements Serializable {
    @Serial
    private static final long serialVersionUID = -5369851314527808712L;

    @NotBlank(message = "搜索关键字不能为空")
    private String keyword;

    /**
     * 文件类型
     */
    private String fileTypes;
}
