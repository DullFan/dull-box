package com.dullfan.system.entity.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件转移参数实体对象
 */
@Data
public class TransferFilePO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6670543331252239384L;

    /**
     * 要转移的文件ID,多个使用公用分隔符隔开
     */
    private String fileIds;

    /**
     * 要转移到哪个文件夹下面
     */
    private String targetParentId;

}
