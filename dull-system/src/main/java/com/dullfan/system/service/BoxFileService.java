package com.dullfan.system.service;

import java.util.List;

import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.system.entity.po.BoxFile;
import com.dullfan.system.entity.query.BoxFileQuery;

public interface BoxFileService {

    /**
     * 根据条件查询列表
     */
    List<BoxFile> selectListByParam(BoxFileQuery param);

    /**
     * 根据条件查询列表数量
     */
    Integer selectCountByParam(BoxFileQuery param);

    /**
     * 分页查询
     */
    PaginationResultVo<BoxFile> selectListByPage(BoxFileQuery param);

    /**
     * 新增
     */
    Integer insert(BoxFile bean);

    /**
     * 批量新增
     */
    Integer insertBatch(List<BoxFile> listBean);

    /**
     * 根据 FileId 查询
     */
     BoxFile selectByFileId(Long fileId);

    /**
     * 根据 FileId 修改
     */
    Integer updateByFileId(BoxFile bean,Long fileId);


    /**
     * 根据 FileId 删除
     */
    Integer deleteByFileId(Long fileId);

    /**
     * 根据 FileId 删除
     */
    Integer deleteByFileIds(List<Long> fileIds);

    List<BoxFile> selectByFileIds(List<Long> fileIds);
}