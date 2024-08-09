package com.dullfan.system.entity.dto;

import com.dullfan.system.entity.po.BoxShare;
import com.dullfan.system.entity.vo.ShareSimpleDetailVO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class QueryShareSimpleDetailDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -176409620147597707L;

    /**
     * 分享ID
     */
    private Long shareId;

    /**
     * 分享实体信息
     */
    private BoxShare record;

    /**
     * 简单分享详情信息
     */
    private ShareSimpleDetailVO vo;
}
