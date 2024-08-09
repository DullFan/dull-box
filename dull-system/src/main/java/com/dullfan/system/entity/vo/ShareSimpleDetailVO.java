package com.dullfan.system.entity.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ShareSimpleDetailVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -5193052911661097422L;

    /**
     * 分享ID
     */
    private Long shareId;
    /**
     * 分享名称
     */
    private String shareName;
    /**
     * 分享者信息
     */
    private ShareUserInfoVO shareUserInfoVO;
}
