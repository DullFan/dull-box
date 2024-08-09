package com.dullfan.system.entity.po;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CancelSharePO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4946860490884392275L;

    @NotBlank(message = "请选择要取消的分享")
    private String shareIds;


}
