package com.dullfan.system.entity.dto;

import com.dullfan.system.entity.po.BoxShare;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CheckShareCodeDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -8392299176456107610L;

    /**
     * 分享ID
     */
    private Long shareId;

    /**
     * 分享码
     */
    private String shareCode;

    /**
     * 分享信息实体文件
     */
    private BoxShare boxShare;
}
