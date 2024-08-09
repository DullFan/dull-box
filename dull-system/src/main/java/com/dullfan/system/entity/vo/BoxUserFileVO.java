package com.dullfan.system.entity.vo;

import com.dullfan.common.utils.serializer.IdEncryptSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author DullFan
 */
@Data
public class BoxUserFileVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6974239262923121839L;

    /**
     * 文件记录ID
     */
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long fileId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 上级文件夹ID,顶级文件夹为0
     */
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long parentId;

    /**
     * 真实文件id
     */
    private Long realFileId;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 父文件夹名称
     */
    private String parentFilename;

    /**
     * 是否是文件夹 （0 否 1 是）
     */
    private Integer folderFlag;

    /**
     * 文件大小展示字符
     */
    private String fileSizeDesc;

    /**
     * 文件类型（1 普通文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件 12 csv）
     */
    private Integer fileType;

    /**
     * 删除标识（0 否 1 是）
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
