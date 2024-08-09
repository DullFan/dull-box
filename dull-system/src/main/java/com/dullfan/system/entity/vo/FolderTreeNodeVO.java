package com.dullfan.system.entity.vo;

import com.dullfan.common.utils.serializer.IdEncryptSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class FolderTreeNodeVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1809288521718199210L;

    /**
     * 文件夹名称
     */
    private String label;

    /**
     * 文件ID
     */
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long id;

    /**
     * 父文件ID
     */
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long parentId;

    /**
     * 子节点集合
     */
    private List<FolderTreeNodeVO> children;
}
