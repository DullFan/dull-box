package com.dullfan.system.service;

import java.util.Date;
import java.util.List;

import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.system.entity.po.BoxUserSearchHistory;
import com.dullfan.system.entity.query.BoxUserSearchHistoryQuery;

public interface BoxUserSearchHistoryService {

    /**
     * 根据条件查询列表
     */
    List<BoxUserSearchHistory> selectListByParam(BoxUserSearchHistoryQuery param);

    /**
     * 根据条件查询列表数量
     */
    Integer selectCountByParam(BoxUserSearchHistoryQuery param);

    /**
     * 分页查询
     */
    PaginationResultVo<BoxUserSearchHistory> selectListByPage(BoxUserSearchHistoryQuery param);

    /**
     * 新增
     */
    Integer insert(BoxUserSearchHistory bean);

    /**
     * 批量新增
     */
    Integer insertBatch(List<BoxUserSearchHistory> listBean);

    /**
     * 根据 Id 查询
     */
     BoxUserSearchHistory selectById(Long id);

    /**
     * 根据 Id 修改
     */
    Integer updateById(BoxUserSearchHistory bean,Long id);


    /**
     * 根据 Id 删除
     */
    Integer deleteById(Long id);

    /**
     * 根据 Id 批量删除
     */
    Integer deleteByIdBatch(List<Integer> list);
    /**
     * 根据 UserIdAndSearchContentAndUpdateTime 查询
     */
     BoxUserSearchHistory selectByUserIdAndSearchContentAndUpdateTime(Long userId, String searchContent, Date updateTime);

    /**
     * 根据 UserIdAndSearchContentAndUpdateTime 修改
     */
    Integer updateByUserIdAndSearchContentAndUpdateTime(BoxUserSearchHistory bean,Long userId,String searchContent,Date updateTime);


    /**
     * 根据 UserIdAndSearchContentAndUpdateTime 删除
     */
    Integer deleteByUserIdAndSearchContentAndUpdateTime(Long userId,String searchContent,Date updateTime);

    /**
     * 根据 UserIdAndSearchContent 查询
     */
     BoxUserSearchHistory selectByUserIdAndSearchContent(Long userId,String searchContent);

    /**
     * 根据 UserIdAndSearchContent 修改
     */
    Integer updateByUserIdAndSearchContent(BoxUserSearchHistory bean,Long userId,String searchContent);


    /**
     * 根据 UserIdAndSearchContent 删除
     */
    Integer deleteByUserIdAndSearchContent(Long userId,String searchContent);

    List<BoxUserSearchHistory> selectByUserId(Long userId);

}