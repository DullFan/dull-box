package com.dullfan.system.service.impl;

import java.util.List;

import com.dullfan.common.entity.query.SimplePage;
import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.common.enums.PageSizeEnum;
import com.dullfan.system.entity.po.BoxErrorLog;
import com.dullfan.system.entity.query.BoxErrorLogQuery;
import com.dullfan.system.mappers.BoxErrorLogMapper;
import com.dullfan.system.service.BoxErrorLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("BoxErrorLogService")
public class BoxErrorLogServiceImpl implements BoxErrorLogService  {

    @Resource
    BoxErrorLogMapper boxErrorLogMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<BoxErrorLog> selectListByParam(BoxErrorLogQuery param){
        return boxErrorLogMapper.selectList(param);
    }

    /**
     * 根据条件查询列表数量
     */
    @Override
    public Integer selectCountByParam(BoxErrorLogQuery param){
        return boxErrorLogMapper.selectCount(param);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVo<BoxErrorLog> selectListByPage(BoxErrorLogQuery param){
        int count = selectCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNum(), count, pageSize);
        param.setSimplePage(page);
        List<BoxErrorLog> list = selectListByParam(param);
        return new PaginationResultVo<>(count, page.getPageSize(), page.getPageNum(), page.getPageTotal(), list);
    }

    /**
     * 新增
     */
    @Override
    public Integer insert(BoxErrorLog bean){
        return boxErrorLogMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer insertBatch(List<BoxErrorLog> listBean){
        return boxErrorLogMapper.insertBatch(listBean);
    }

    /**
     * 根据 Id 查询
     */
    @Override
    public BoxErrorLog selectById(Long id){
        return boxErrorLogMapper.selectById(id);
    }

    /**
     * 根据 Id 修改
     */
    @Override
    public Integer updateById(BoxErrorLog bean,Long id){
        BoxErrorLogQuery query = new BoxErrorLogQuery();
        query.setId(id);
        return boxErrorLogMapper.updateByParam(bean,query);
    }


    /**
     * 根据 Id 删除
     */
    @Override
    public Integer deleteById(Long id){
        return boxErrorLogMapper.deleteById(id);
    }
    /**
     * 根据 Id 批量删除
     */
    @Override
    public Integer deleteByIdBatch(List<Integer> list){
        return boxErrorLogMapper.deleteByIdBatch(list);
    }
}