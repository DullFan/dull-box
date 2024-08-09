package com.dullfan.system.entity.dto;

import com.dullfan.system.entity.po.BoxShare;
import com.dullfan.system.entity.vo.ShareDetailVO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class QueryShareDetailDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 9085542343689011965L;

    /**
     * 对应的分享ID
     */
    private Long shareId;

    /**
     * 分享实体
     */
    private BoxShare boxShare;

    /**
     * 分享详情的VO对象
     */
    private ShareDetailVO vo;
}
