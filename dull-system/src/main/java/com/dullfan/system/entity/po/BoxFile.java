package com.dullfan.system.entity.po;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author DullFan
 * @date 2024-07-15 11:25:06
 */
@Data
public class BoxFile implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件物理路径
     */
    private String realPath;

    /**
     * 文件实际大小
     */
    private String fileSize;

    /**
     * 文件大小展示字符
     */
    private String fileSizeDesc;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 文件预览的响应头Content-Type的值
     */
    private String filePreviewContentType;

    /**
     * 文件唯一标识
     */
    private String identifier;

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

}
