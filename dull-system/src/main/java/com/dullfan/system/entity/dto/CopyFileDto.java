package com.dullfan.system.entity.dto;

import com.dullfan.system.entity.po.BoxUserFile;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 文件复制参数实体对象
 */
@Data
public class CopyFileDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -7957749236287968894L;

    /**
     * 要复制的文件ID
     */
    private List<Long> fileIds;

    /**
     * 要复制到哪个文件夹下面
     */
    private Long targetParentId;

    /**
     * 要复制的文件列表
     */
    private List<BoxUserFile> prepareRecords;

    /**
     * 用户ID
     */
    private Long userId;

}