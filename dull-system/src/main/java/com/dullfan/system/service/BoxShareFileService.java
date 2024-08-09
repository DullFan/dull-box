package com.dullfan.system.service;

import java.util.List;

import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.system.entity.po.BoxShareFile;
import com.dullfan.system.entity.query.BoxShareFileQuery;

public interface BoxShareFileService {

    /**
     * 根据条件查询列表
     */
    List<BoxShareFile> selectListByParam(BoxShareFileQuery param);

    /**
     * 根据条件查询列表
     */
    List<BoxShareFile> selectAllList(BoxShareFileQuery param);

    /**
     * 根据条件查询列表数量
     */
    Integer selectCountByParam(BoxShareFileQuery param);

    /**
     * 分页查询
     */
    PaginationResultVo<BoxShareFile> selectListByPage(BoxShareFileQuery param);

    /**
     * 新增
     */
    Integer insert(BoxShareFile bean);

    /**
     * 批量新增
     */
    Integer insertBatch(List<BoxShareFile> listBean);

    /**
     * 根据 Id 查询
     */
     BoxShareFile selectById(Long id);

    /**
     * 根据 Id 修改
     */
    Integer updateById(BoxShareFile bean,Long id);


    /**
     * 根据 Id 删除
     */
    Integer deleteById(Long id);

    /**
     * 根据 Id 批量删除
     */
    Integer deleteByIdBatch(List<Integer> list);
    /**
     * 根据 ShareIdAndFileId 查询
     */
     BoxShareFile selectByShareIdAndFileId(Long shareId,Long fileId);

    /**
     * 根据 ShareIdAndFileId 修改
     */
    Integer updateByShareIdAndFileId(BoxShareFile bean,Long shareId,Long fileId);


    /**
     * 根据 ShareIdAndFileId 删除
     */
    Integer deleteByShareIdAndFileId(Long shareId,Long fileId);

    /**
     * 根据ShareId删除
     */
    Integer deleteByShareIds(List<Long> shareIds);

    List<BoxShareFile> selectByFileIds(List<Long> allAvailableFileIdList);
}