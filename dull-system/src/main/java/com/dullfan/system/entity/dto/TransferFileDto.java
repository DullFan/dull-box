package com.dullfan.system.entity.dto;

import com.dullfan.system.entity.po.BoxUserFile;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 文件转移参数实体对象
 */
@Data
public class TransferFileDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 6670543331252239384L;

    /**
     * 要转移的文件ID
     */
    private List<Long> fileIds;

    /**
     * 要转移到哪个文件夹下面
     */
    private Long targetParentId;

    /**
     * 要转移的文件列表
     */
    private List<BoxUserFile> prepareRecords;

}
