package com.dullfan.system.entity.po;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateFilenamePO implements Serializable {

    private static final long serializableVersionUID = -1297361284524L;

    /**
     * 文件ID
     */
    @NotBlank(message = "更新的文件ID不能为空")
    private String fileId;

    /**
     * 新文件名称
     */
    @NotBlank(message = "文件名称不能为空")
    private String newFilename;

    /**
     * 用户ID
     */
    private Long userId;
}
