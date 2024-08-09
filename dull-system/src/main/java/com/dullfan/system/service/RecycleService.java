package com.dullfan.system.service;

import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.system.entity.po.BoxUserFile;
import com.dullfan.system.entity.po.DeleteFilePO;
import com.dullfan.system.entity.vo.BoxUserFileVO;

import java.util.List;

public interface RecycleService {

    /**
     * 查询用户回收站列表
     */
    List<BoxUserFile> recycles();

    /**
     * 物理删除文件
     */
    void delete(DeleteFilePO deleteFilePO);
}
