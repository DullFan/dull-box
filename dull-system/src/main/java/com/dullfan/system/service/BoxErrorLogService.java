package com.dullfan.system.service;

import java.util.List;

import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.system.entity.po.BoxErrorLog;
import com.dullfan.system.entity.query.BoxErrorLogQuery;

public interface BoxErrorLogService {

    /**
     * 根据条件查询列表
     */
    List<BoxErrorLog> selectListByParam(BoxErrorLogQuery param);

    /**
     * 根据条件查询列表数量
     */
    Integer selectCountByParam(BoxErrorLogQuery param);

    /**
     * 分页查询
     */
    PaginationResultVo<BoxErrorLog> selectListByPage(BoxErrorLogQuery param);

    /**
     * 新增
     */
    Integer insert(BoxErrorLog bean);

    /**
     * 批量新增
     */
    Integer insertBatch(List<BoxErrorLog> listBean);

    /**
     * 根据 Id 查询
     */
     BoxErrorLog selectById(Long id);

    /**
     * 根据 Id 修改
     */
    Integer updateById(BoxErrorLog bean,Long id);


    /**
     * 根据 Id 删除
     */
    Integer deleteById(Long id);

    /**
     * 根据 Id 批量删除
     */
    Integer deleteByIdBatch(List<Integer> list);
}