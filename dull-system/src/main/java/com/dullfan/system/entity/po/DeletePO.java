package com.dullfan.system.entity.po;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class DeletePO implements Serializable {


    @Serial
    private static final long serialVersionUID = -1637449093340693583L;
    /**
     * 文件ID
     */
    @NotNull(message = "文件夹名称不能为空")
    private List<String> fileIds;

    /**
     * 用户ID
     */
    private Long userId;
}
