package com.dullfan.system.entity.po;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author DullFan
 */
@Data
public class BoxShareFile implements Serializable {


    @Serial
    private static final long serialVersionUID = -100469236863867530L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 分享id
     */
    private Long shareId;

    /**
     * 文件记录ID
     */
    private Long fileId;

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

}
