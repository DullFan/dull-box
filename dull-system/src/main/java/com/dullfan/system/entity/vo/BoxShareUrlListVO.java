package com.dullfan.system.entity.vo;

import com.dullfan.common.utils.serializer.IdEncryptSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class BoxShareUrlListVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6614316443048818149L;

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
     * 分享URL
     */
    private String shareUrl;
    /**
     * 分享码
     */
    private String shareCode;
    /**
     * 分享类型
     */
    private Integer shareStatus;
    /**
     * 分享的过期类型
     */
    private Integer shareType;
    /**
     * 分享的过期类型
     */
    private Integer shareDayType;
    /**
     * 分享的过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shareEndTime;
    /**
     * 分享的创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
