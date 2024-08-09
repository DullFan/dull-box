package com.dullfan.system.entity.query;

import com.dullfan.common.entity.query.BaseParam;
import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author DullFan
 * @date 2024-07-15 11:25:06
 */
@Data
public class BoxShareQuery extends BaseParam {

    
    /**
     * 分享id
     */
    private Long shareId;

    /**
     * 分享名称
     */
    private String shareName;

    /**
     * 分享类型（0 有提取码）
     */
    private Integer shareType;

    /**
     * 分享类型（0 永久有效；1 7天有效；2 30天有效）
     */
    private Integer shareDayType;

    /**
     * 分享有效天数（永久有效为0）
     */
    private Integer shareDay;

    /**
     * 分享结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shareEndTime;

    /**
     * 分享链接地址
     */
    private String shareUrl;

    /**
     * 分享提取码
     */
    private String shareCode;

    /**
     * 分享状态（0 正常；1 有文件被删除）
     */
    private Integer shareStatus;

    /**
     * 分享创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    private String shareNameFuzzy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shareEndTimeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shareEndTimeEnd;

    private String shareUrlFuzzy;

    private String shareCodeFuzzy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;


}
