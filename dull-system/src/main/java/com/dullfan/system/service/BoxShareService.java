package com.dullfan.system.service;

import java.util.List;

import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.system.entity.po.BoxShare;
import java.util.Date;
import com.dullfan.system.entity.query.BoxShareQuery;
import com.dullfan.system.entity.vo.BoxShareUrlListVO;

public interface BoxShareService {

    /**
     * 根据条件查询列表
     */
    List<BoxShare> selectListByParam(BoxShareQuery param);

    /**
     * 根据条件查询列表数量
     */
    Integer selectCountByParam(BoxShareQuery param);

    /**
     * 分页查询
     */
    PaginationResultVo<BoxShare> selectListByPage(BoxShareQuery param);

    /**
     * 新增
     */
    Integer insert(BoxShare bean);

    /**
     * 批量新增
     */
    Integer insertBatch(List<BoxShare> listBean);

    /**
     * 根据 ShareId 查询
     */
     BoxShare selectByShareId(Long shareId);

    /**
     * 根据 ShareId 修改
     */
    Integer updateByShareId(BoxShare bean,Long shareId);


    /**
     * 根据 ShareId 删除
     */
    Integer deleteByShareId(Long shareId);

    /**
     * 根据 CreateUserAndCreateTime 查询
     */
     BoxShare selectByCreateUserAndCreateTime(Long createUser,Date createTime);

    /**
     * 根据 CreateUserAndCreateTime 修改
     */
    Integer updateByCreateUserAndCreateTime(BoxShare bean,Long createUser,Date createTime);


    /**
     * 根据 CreateUserAndCreateTime 删除
     */
    Integer deleteByCreateUserAndCreateTime(Long createUser,Date createTime);

    /**
     * 查询用户的分享列表
     */
    List<BoxShareUrlListVO> selectShareVOListByUserId(Long userId);

    List<BoxShare> selectByShareIds(List<Long> shareIds);

    Integer deleteByShareIds(List<Long> shareIds);

    /**
     * 滚动查询数据
     */
    List<Long> rollingQueryShareId(long startId, long limit);
}