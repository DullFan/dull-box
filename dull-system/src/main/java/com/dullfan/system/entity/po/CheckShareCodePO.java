package com.dullfan.system.entity.po;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CheckShareCodePO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8392299176456107610L;

    @NotBlank(message = "分享ID不能为空")
    private String shareId;

    @NotBlank(message = "分享码不能为空")
    private String shareCode;
}
