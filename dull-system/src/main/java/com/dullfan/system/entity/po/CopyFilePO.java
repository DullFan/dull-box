package com.dullfan.system.entity.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件复制参数实体
 */
@Data
public class CopyFilePO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4522951262344708467L;

    /**
     * 要复制的文件I
     */
    private String fileIds;

    /**
     * 要复制到哪个文件夹下面
     */
    private String targetParentId;
}
