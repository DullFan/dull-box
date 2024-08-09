package com.dullfan.system.entity.dto;

import com.dullfan.system.entity.po.BoxShare;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class CreateShareUrlDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 5131756493422738429L;

    /**
     * 分享名称
     */
    private String shareName;

    /**
     * 分享类型
     */
    private Integer shareType;

    /**
     * 分享日期类型
     */
    private Integer shareDayType;

    /**
     * 分享文件ID集合
     */
    private List<Long> shareFIleIds;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 已经保存的分享实体信息
     */
    private BoxShare record;
}
