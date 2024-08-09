package com.dullfan.system.entity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ShareSaveFilesDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -7970283677565703627L;

    /**
     * 要保存的文件ID列表
     */
    private List<Long> fileIds;

    /**
     * 要保存的文件ID列表
     */
    private Long targetParentId;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    /**
     * 分享的ID
     */
    private Long shareId;

}
