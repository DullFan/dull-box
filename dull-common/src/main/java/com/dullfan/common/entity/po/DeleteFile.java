package com.dullfan.common.entity.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 文件存储引擎物理文件实体
 */
@Data
public class DeleteFile implements Serializable {

    @Serial
    private static final long serialVersionUID = -5794697339078761384L;
    /**
     * 要删除的物理文件路径的集合
     */
    private List<String> realFilePathList;

}
