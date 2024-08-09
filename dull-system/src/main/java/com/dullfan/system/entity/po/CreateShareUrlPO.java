package com.dullfan.system.entity.po;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建分享链接的返回实体
 */
@Data
public class CreateShareUrlPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5717625024534437980L;

    /**
     * 分享名称
     */
    @NotBlank(message = "分享名称不能为空")
    private String shareName;

    /**
     * 分享类型
     */
    @NotNull(message = "分享类型不能为空")
    private Integer shareType;

    /**
     * 分享日期类型
     */
    @NotNull(message = "分享日期类型不能为空")
    private Integer shareDayType;

    /**
     * 分享文件ID集合
     */
    @NotNull(message = "分享文件ID集合不能为空")
    private String shareFileIds;
}
