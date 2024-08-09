package com.dullfan.system.entity.dto;

import com.dullfan.system.entity.po.BoxFile;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 7222880247243672003L;

    /**
     * 文件唯一标识
     */
    private String identifier;

    /**
     * 父文件ID
     */
    private String parentId;

    /**
     * 文件
     */
    private MultipartFile file;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件大小
     */
    private Long totalSize;

    /**
     * 实体文件记录
     */
    private BoxFile record;

    /**
     * 文件上传的物理路径
     */
    private String realPath;
}
