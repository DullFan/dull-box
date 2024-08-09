package com.dullfan.common.enums;

import lombok.Data;

import java.io.OutputStream;
import java.io.Serial;
import java.io.Serializable;

/**
 * 文件读取的实体信息
 */
@Data
public class ReadFile implements Serializable {
    @Serial
    private static final long serialVersionUID = -4149547136020211253L;

    /**
     * 文件的真实存储路径
     */
    private String realPath;

    /**
     * 文件的输出流
     */
    private OutputStream outputStream;
}
