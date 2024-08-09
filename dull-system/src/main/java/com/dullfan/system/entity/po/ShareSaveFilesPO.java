package com.dullfan.system.entity.po;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ShareSaveFilesPO implements Serializable {
    @Serial
    private static final long serialVersionUID = -3890340863186450365L;

    /**
     * 要转存的ID集合
     */
    @NotBlank(message = "请选择要转存的文件")
    private String fileIds;

    /**
     * 目标文件夹ID
     */
    @NotBlank(message = "请选择要保存的文件夹")
    private String targetFileId;

}
