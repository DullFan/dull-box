package com.dullfan.system.entity.po;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class DeleteFilePO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6585156518486476129L;

    /**
     * 删除文件ID
     */
    @NotNull(message = "文件夹名称不能为空")
    private List<String> fileIds;

    /**
     * 用户ID
     */
    private Long userId;
}
