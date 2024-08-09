package com.dullfan.system.entity.po;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件预览实体
 */
@Data
public class FilePreview implements Serializable {
    @Serial
    private static final long serialVersionUID = -5416787478568088536L;

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
