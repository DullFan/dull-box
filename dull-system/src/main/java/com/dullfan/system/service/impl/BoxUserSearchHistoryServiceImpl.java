package com.dullfan.system.service.impl;

import java.util.List;

import com.dullfan.common.entity.query.SimplePage;
import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.common.enums.PageSizeEnum;
import com.dullfan.system.entity.po.BoxUserSearchHistory;
import com.dullfan.system.entity.query.BoxUserSearchHistoryQuery;
import com.dullfan.system.mappers.BoxUserSearchHistoryMapper;
import com.dullfan.system.service.BoxUserSearchHistoryService;
import java.util.Date;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("BoxUserSearchHistoryService")
public class BoxUserSearchHistoryServiceImpl implements BoxUserSearchHistoryService {

    @Resource
    BoxUserSearchHistoryMapper boxUserSearchHistoryMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<BoxUserSearchHistory> selectListByParam(BoxUserSearchHistoryQuery param) {
        return boxUserSearchHistoryMapper.selectList(param);
    }

    /**
     * 根据条件查询列表数量
     */
    @Override
    public Integer selectCountByParam(BoxUserSearchHistoryQuery param) {
        return boxUserSearchHistoryMapper.selectCount(param);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVo<BoxUserSearchHistory> selectListByPage(BoxUserSearchHistoryQuery param) {
        int count = selectCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNum(), count, pageSize);
        param.setSimplePage(page);
        List<BoxUserSearchHistory> list = selectListByParam(param);
        return new PaginationResultVo<>(count, page.getPageSize(), page.getPageNum(), page.getPageTotal(), list);
    }

    /**
     * 新增
     */
    @Override
    public Integer insert(BoxUserSearchHistory bean) {
        return boxUserSearchHistoryMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer insertBatch(List<BoxUserSearchHistory> listBean) {
        return boxUserSearchHistoryMapper.insertBatch(listBean);
    }

    /**
     * 根据 Id 查询
     */
    @Override
    public BoxUserSearchHistory selectById(Long id) {
        return boxUserSearchHistoryMapper.selectById(id);
    }

    /**
     * 根据 Id 修改
     */
    @Override
    public Integer updateById(BoxUserSearchHistory bean, Long id) {
        BoxUserSearchHistoryQuery query = new BoxUserSearchHistoryQuery();
        query.setId(id);
        return boxUserSearchHistoryMapper.updateByParam(bean, query);
    }


    /**
     * 根据 Id 删除
     */
    @Override
    public Integer deleteById(Long id) {
        return boxUserSearchHistoryMapper.deleteById(id);
    }

    /**
     * 根据 Id 批量删除
     */
    @Override
    public Integer deleteByIdBatch(List<Integer> list) {
        return boxUserSearchHistoryMapper.deleteByIdBatch(list);
    }

    /**
     * 根据 UserIdAndSearchContentAndUpdateTime 查询
     */
    @Override
    public BoxUserSearchHistory selectByUserIdAndSearchContentAndUpdateTime(Long userId, String searchContent, Date updateTime) {
        return boxUserSearchHistoryMapper.selectByUserIdAndSearchContentAndUpdateTime(userId, searchContent, updateTime);
    }

    /**
     * 根据 UserIdAndSearchContentAndUpdateTime 修改
     */
    @Override
    public Integer updateByUserIdAndSearchContentAndUpdateTime(BoxUserSearchHistory bean, Long userId, String searchContent, Date updateTime) {
        BoxUserSearchHistoryQuery query = new BoxUserSearchHistoryQuery();
        query.setUserId(userId);
        query.setSearchContent(searchContent);
        query.setUpdateTime(updateTime);
        return boxUserSearchHistoryMapper.updateByParam(bean, query);
    }


    /**
     * 根据 UserIdAndSearchContentAndUpdateTime 删除
     */
    @Override
    public Integer deleteByUserIdAndSearchContentAndUpdateTime(Long userId, String searchContent, Date updateTime) {
        return boxUserSearchHistoryMapper.deleteByUserIdAndSearchContentAndUpdateTime(userId, searchContent, updateTime);
    }

    /**
     * 根据 UserIdAndSearchContent 查询
     */
    @Override
    public BoxUserSearchHistory selectByUserIdAndSearchContent(Long userId, String searchContent) {
        return boxUserSearchHistoryMapper.selectByUserIdAndSearchContent(userId, searchContent);
    }

    /**
     * 根据 UserIdAndSearchContent 修改
     */
    @Override
    public Integer updateByUserIdAndSearchContent(BoxUserSearchHistory bean, Long userId, String searchContent) {
        BoxUserSearchHistoryQuery query = new BoxUserSearchHistoryQuery();
        query.setUserId(userId);
        query.setSearchContent(searchContent);
        return boxUserSearchHistoryMapper.updateByParam(bean, query);
    }


    /**
     * 根据 UserIdAndSearchContent 删除
     */
    @Override
    public Integer deleteByUserIdAndSearchContent(Long userId, String searchContent) {
        return boxUserSearchHistoryMapper.deleteByUserIdAndSearchContent(userId, searchContent);
    }

    @Override
    public List<BoxUserSearchHistory> selectByUserId(Long userId) {
        return boxUserSearchHistoryMapper.selectByUserId(userId);
    }
}