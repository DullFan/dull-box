package com.dullfan.system.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 文件搜索参数实体
 */
@Data
public class FileSearchDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -5369851314527808712L;

    @NotBlank(message = "搜索关键字不能为空")
    private String keyword;

    /**
     * 搜索文件类型集合
     */
    private List<Integer> fileTypes;

    /**
     * 用户ID
     */
    private Long userId;
}
