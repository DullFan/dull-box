package com.dullfan.system.entity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class BoxUserFileDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -696452691330758662L;

    /**
     * 父文件夹ID
     */
    private Long parentId;

    /**
     * 文件类型的集合
     */
    private List<Integer> fileTypeArray;

    /**
     * 当前登录的用户
     */
    private Long userId;

    /**
     * 文件的删除标识
     */
    private Integer delFlag;
}
