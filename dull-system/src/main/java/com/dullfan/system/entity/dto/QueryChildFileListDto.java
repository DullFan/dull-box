package com.dullfan.system.entity.dto;

import com.dullfan.system.entity.po.BoxShare;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class QueryChildFileListDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 5029036739105817343L;
    /**
     * 分享的ID
     */
    private Long shareId;

    /**
     * 父文件夹ID
     */
    private Long parentId;

    /**
     * 分享对应的实体信息
     */
    private BoxShare record;

}
