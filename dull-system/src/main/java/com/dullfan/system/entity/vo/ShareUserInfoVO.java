package com.dullfan.system.entity.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ShareUserInfoVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7396781726042559442L;

    /**
     * 分享者ID
     */
    private Long userId;

    /**
     * 分享者名称
     */
    private String userName;

}
