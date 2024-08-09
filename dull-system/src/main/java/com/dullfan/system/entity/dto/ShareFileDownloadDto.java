package com.dullfan.system.entity.dto;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ShareFileDownloadDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -6644999214177107607L;

    /**
     * 要下载的文件ID
     */
    private Long fileId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 分享ID
     */
    private Long shareId;
    /**
     * 响应
     */
    private HttpServletResponse response;

}
