package com.dullfan.system.entity.vo;

import com.dullfan.common.utils.serializer.IdEncryptSerializer;
import com.dullfan.system.entity.po.BoxUserFile;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ShareDetailVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6064584165575786590L;

    /**
     * 分享ID
     */
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long shareId;
    /**
     * 分享名称
     */
    private String shareName;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 分享过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shareEndTime;
    /**
     * 过期天数
     */
    private Integer shareDay;
    /**
     * 分享文件列表
     */
    private List<BoxUserFileVO> boxUserFileVOList;
    /**
     * 分享者信息
     */
    private ShareUserInfoVO shareUserInfoVO;
}
