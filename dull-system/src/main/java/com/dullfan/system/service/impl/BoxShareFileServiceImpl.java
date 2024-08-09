package com.dullfan.system.service.impl;

import java.util.List;

import com.dullfan.common.entity.query.SimplePage;
import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.common.enums.PageSizeEnum;
import com.dullfan.common.utils.SecurityUtils;
import com.dullfan.system.entity.po.BoxShareFile;
import com.dullfan.system.entity.query.BoxShareFileQuery;
import com.dullfan.system.mappers.BoxShareFileMapper;
import com.dullfan.system.service.BoxShareFileService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("BoxShareFileService")
public class BoxShareFileServiceImpl implements BoxShareFileService  {

    @Resource
    BoxShareFileMapper boxShareFileMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<BoxShareFile> selectListByParam(BoxShareFileQuery param){
        return boxShareFileMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public List<BoxShareFile> selectAllList(BoxShareFileQuery param){
        return boxShareFileMapper.selectAllList(param);
    }

    /**
     * 根据条件查询列表数量
     */
    @Override
    public Integer selectCountByParam(BoxShareFileQuery param){
        return boxShareFileMapper.selectCount(param);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVo<BoxShareFile> selectListByPage(BoxShareFileQuery param){
        int count = selectCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNum(), count, pageSize);
        param.setSimplePage(page);
        List<BoxShareFile> list = selectListByParam(param);
        return new PaginationResultVo<>(count, page.getPageSize(), page.getPageNum(), page.getPageTotal(), list);
    }

    /**
     * 新增
     */
    @Override
    public Integer insert(BoxShareFile bean){
        return boxShareFileMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer insertBatch(List<BoxShareFile> listBean){
        return boxShareFileMapper.insertBatch(listBean);
    }

    /**
     * 根据 Id 查询
     */
    @Override
    public BoxShareFile selectById(Long id){
        return boxShareFileMapper.selectById(id);
    }

    /**
     * 根据 Id 修改
     */
    @Override
    public Integer updateById(BoxShareFile bean,Long id){
        BoxShareFileQuery query = new BoxShareFileQuery();
        query.setId(id);
        return boxShareFileMapper.updateByParam(bean,query);
    }


    /**
     * 根据 Id 删除
     */
    @Override
    public Integer deleteById(Long id){
        return boxShareFileMapper.deleteById(id);
    }
    /**
     * 根据 Id 批量删除
     */
    @Override
    public Integer deleteByIdBatch(List<Integer> list){
        return boxShareFileMapper.deleteByIdBatch(list);
    }
    /**
     * 根据 ShareIdAndFileId 查询
     */
    @Override
    public BoxShareFile selectByShareIdAndFileId(Long shareId,Long fileId){
        return boxShareFileMapper.selectByShareIdAndFileId(shareId,fileId);
    }

    /**
     * 根据 ShareIdAndFileId 修改
     */
    @Override
    public Integer updateByShareIdAndFileId(BoxShareFile bean,Long shareId,Long fileId){
        BoxShareFileQuery query = new BoxShareFileQuery();
        query.setShareId(shareId);
        query.setFileId(fileId);
        return boxShareFileMapper.updateByParam(bean,query);
    }


    /**
     * 根据 ShareIdAndFileId 删除
     */
    @Override
    public Integer deleteByShareIdAndFileId(Long shareId,Long fileId){
        return boxShareFileMapper.deleteByShareIdAndFileId(shareId,fileId);
    }

    @Override
    public Integer deleteByShareIds(List<Long> shareIds) {
        return boxShareFileMapper.deleteByShareIds(shareIds, SecurityUtils.getUserId());
    }

    @Override
    public List<BoxShareFile> selectByFileIds(List<Long> allAvailableFileIdList) {
        return boxShareFileMapper.selectByFileIds(allAvailableFileIdList);
    }
}