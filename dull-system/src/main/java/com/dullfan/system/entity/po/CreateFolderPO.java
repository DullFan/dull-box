package com.dullfan.system.entity.po;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.security.SecureRandomParameters;

@Data
public class CreateFolderPO implements Serializable {
    private static final long serializableVersion = 5454545121354654654L;

    /**
     * 父文件ID
     */
    @NotBlank(message = "父文件ID不能为空")
    private String parentId;

    /**
     * 文件夹名称
     */
    @NotBlank(message = "文件夹名称不能为空")
    private String fileName;

    /**
     * 用户ID
     */
    private Long userId;
}
