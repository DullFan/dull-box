package com.dullfan.system.entity.po;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件下载的实体
 */
@Data
public class FileDownload implements Serializable {
    @Serial
    private static final long serialVersionUID = 8548821236690356496L;

    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 请求响应对象
     */
    private HttpServletResponse response;

    /**
     * 用户ID
     */
    private Long userId;

}
