package com.dullfan.system.service.impl;

import java.util.Date;
import java.util.List;

import com.dullfan.common.entity.query.SimplePage;
import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.common.enums.PageSizeEnum;
import com.dullfan.common.utils.SecurityUtils;
import com.dullfan.system.entity.po.BoxShare;
import com.dullfan.system.entity.query.BoxShareQuery;
import com.dullfan.system.entity.vo.BoxShareUrlListVO;
import com.dullfan.system.mappers.BoxShareMapper;
import com.dullfan.system.service.BoxShareService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("BoxShareService")
public class BoxShareServiceImpl implements BoxShareService  {

    @Resource
    BoxShareMapper boxShareMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<BoxShare> selectListByParam(BoxShareQuery param){
        return boxShareMapper.selectList(param);
    }

    /**
     * 根据条件查询列表数量
     */
    @Override
    public Integer selectCountByParam(BoxShareQuery param){
        return boxShareMapper.selectCount(param);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVo<BoxShare> selectListByPage(BoxShareQuery param){
        int count = selectCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNum(), count, pageSize);
        param.setSimplePage(page);
        List<BoxShare> list = selectListByParam(param);
        return new PaginationResultVo<>(count, page.getPageSize(), page.getPageNum(), page.getPageTotal(), list);
    }

    /**
     * 新增
     */
    @Override
    public Integer insert(BoxShare bean){
        return boxShareMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer insertBatch(List<BoxShare> listBean){
        return boxShareMapper.insertBatch(listBean);
    }

    /**
     * 根据 ShareId 查询
     */
    @Override
    public BoxShare selectByShareId(Long shareId){
        return boxShareMapper.selectByShareId(shareId);
    }

    /**
     * 根据 ShareId 修改
     */
    @Override
    public Integer updateByShareId(BoxShare bean,Long shareId){
        BoxShareQuery query = new BoxShareQuery();
        query.setShareId(shareId);
        return boxShareMapper.updateByParam(bean,query);
    }


    /**
     * 根据 ShareId 删除
     */
    @Override
    public Integer deleteByShareId(Long shareId){
        return boxShareMapper.deleteByShareId(shareId);
    }
    /**
     * 根据 CreateUserAndCreateTime 查询
     */
    @Override
    public BoxShare selectByCreateUserAndCreateTime(Long createUser, Date createTime){
        return boxShareMapper.selectByCreateUserAndCreateTime(createUser,createTime);
    }

    /**
     * 根据 CreateUserAndCreateTime 修改
     */
    @Override
    public Integer updateByCreateUserAndCreateTime(BoxShare bean,Long createUser,Date createTime){
        BoxShareQuery query = new BoxShareQuery();
        query.setCreateUser(createUser);
        query.setCreateTime(createTime);
        return boxShareMapper.updateByParam(bean,query);
    }


    /**
     * 根据 CreateUserAndCreateTime 删除
     */
    @Override
    public Integer deleteByCreateUserAndCreateTime(Long createUser,Date createTime){
        return boxShareMapper.deleteByCreateUserAndCreateTime(createUser,createTime);
    }

    /**
     * 查询用户的分享列表
     */
    @Override
    public List<BoxShareUrlListVO> selectShareVOListByUserId(Long userId) {
        return boxShareMapper.selectShareVOListByUserId(userId);
    }

    @Override
    public List<BoxShare> selectByShareIds(List<Long> shareIds) {
        return boxShareMapper.selectByShareIds(shareIds, SecurityUtils.getUserId());
    }

    @Override
    public Integer deleteByShareIds(List<Long> shareIds) {
        return boxShareMapper.deleteByShareIds(shareIds,SecurityUtils.getUserId());
    }

    /**
     * 滚动查询数据
     */
    @Override
    public List<Long> rollingQueryShareId(long startId, long limit) {
        return boxShareMapper.rollingQueryShareId(startId,limit);
    }
}