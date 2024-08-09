package com.dullfan.system.service.impl;

import java.util.List;

import com.dullfan.common.entity.query.SimplePage;
import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.common.enums.PageSizeEnum;
import com.dullfan.system.entity.po.BoxFile;
import com.dullfan.system.entity.query.BoxFileQuery;
import com.dullfan.system.mappers.BoxFileMapper;
import com.dullfan.system.service.BoxFileService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("BoxFileService")
public class BoxFileServiceImpl implements BoxFileService  {

    @Resource
    BoxFileMapper boxFileMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<BoxFile> selectListByParam(BoxFileQuery param){
        return boxFileMapper.selectList(param);
    }

    /**
     * 根据条件查询列表数量
     */
    @Override
    public Integer selectCountByParam(BoxFileQuery param){
        return boxFileMapper.selectCount(param);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVo<BoxFile> selectListByPage(BoxFileQuery param){
        int count = selectCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNum(), count, pageSize);
        param.setSimplePage(page);
        List<BoxFile> list = selectListByParam(param);
        return new PaginationResultVo<>(count, page.getPageSize(), page.getPageNum(), page.getPageTotal(), list);
    }

    /**
     * 新增
     */
    @Override
    public Integer insert(BoxFile bean){
        return boxFileMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer insertBatch(List<BoxFile> listBean){
        return boxFileMapper.insertBatch(listBean);
    }

    /**
     * 根据 FileId 查询
     */
    @Override
    public BoxFile selectByFileId(Long fileId){
        return boxFileMapper.selectByFileId(fileId);
    }

    /**
     * 根据 FileId 修改
     */
    @Override
    public Integer updateByFileId(BoxFile bean,Long fileId){
        BoxFileQuery query = new BoxFileQuery();
        query.setFileId(fileId);
        return boxFileMapper.updateByParam(bean,query);
    }


    /**
     * 根据 FileId 删除
     */
    @Override
    public Integer deleteByFileId(Long fileId){
        return boxFileMapper.deleteByFileId(fileId);
    }

    @Override
    public Integer deleteByFileIds(List<Long> fileIds) {
        return boxFileMapper.deleteByFileIds(fileIds);
    }

    @Override
    public List<BoxFile> selectByFileIds(List<Long> fileIds) {
        return boxFileMapper.selectByFileIds(fileIds);
    }
}