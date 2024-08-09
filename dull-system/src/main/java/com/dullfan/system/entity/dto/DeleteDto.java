package com.dullfan.system.entity.dto;

import com.dullfan.system.entity.po.BoxUserFile;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class DeleteDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 5610405340213484240L;

    /**
     * 要操作的文件ID集合
     */
    private List<Long> fileIdList;
    /**
     * 当前登录的用户ID
     */
    private Long userId;
    /**
     * 要被删除的文件记录列表
     */
    private List<BoxUserFile> records;
    /**
     * 所有要删除的文件记录列表
     */
    private List<BoxUserFile> allRecords;

}
