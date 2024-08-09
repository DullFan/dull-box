package com.dullfan.system.service.impl;

import java.util.List;

import com.dullfan.common.entity.query.SimplePage;
import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.common.enums.PageSizeEnum;
import com.dullfan.system.entity.po.BoxFileChunk;
import com.dullfan.system.entity.query.BoxFileChunkQuery;
import com.dullfan.system.mappers.BoxFileChunkMapper;
import com.dullfan.system.service.BoxFileChunkService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("BoxFileChunkService")
public class BoxFileChunkServiceImpl implements BoxFileChunkService  {

    @Resource
    BoxFileChunkMapper boxFileChunkMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<BoxFileChunk> selectListByParam(BoxFileChunkQuery param){
        return boxFileChunkMapper.selectList(param);
    }

    @Override
    public List<BoxFileChunk> selectListByParamNoPage(BoxFileChunkQuery param) {
        return boxFileChunkMapper.selectListNoPage(param);
    }

    @Override
    public List<BoxFileChunk> selectCleanChunkList(BoxFileChunkQuery param) {
        return boxFileChunkMapper.selectCleanChunkList(param);
    }

    /**
     * 根据条件查询列表数量
     */
    @Override
    public Integer selectCountByParam(BoxFileChunkQuery param){
        return boxFileChunkMapper.selectCount(param);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVo<BoxFileChunk> selectListByPage(BoxFileChunkQuery param){
        int count = selectCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNum(), count, pageSize);
        param.setSimplePage(page);
        List<BoxFileChunk> list = selectListByParam(param);
        return new PaginationResultVo<>(count, page.getPageSize(), page.getPageNum(), page.getPageTotal(), list);
    }

    /**
     * 新增
     */
    @Override
    public Integer insert(BoxFileChunk bean){
        return boxFileChunkMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer insertBatch(List<BoxFileChunk> listBean){
        return boxFileChunkMapper.insertBatch(listBean);
    }

    /**
     * 根据 Id 查询
     */
    @Override
    public BoxFileChunk selectById(Long id){
        return boxFileChunkMapper.selectById(id);
    }

    /**
     * 根据 Id 修改
     */
    @Override
    public Integer updateById(BoxFileChunk bean,Long id){
        BoxFileChunkQuery query = new BoxFileChunkQuery();
        query.setId(id);
        return boxFileChunkMapper.updateByParam(bean,query);
    }


    /**
     * 根据 Id 删除
     */
    @Override
    public Integer deleteById(Long id){
        return boxFileChunkMapper.deleteById(id);
    }
    /**
     * 根据 Id 批量删除
     */
    @Override
    public Integer deleteByIdBatch(List<Long> list){
        return boxFileChunkMapper.deleteByIdBatch(list);
    }
    /**
     * 根据 IdentifierAndChunkNumberAndCreateUser 查询
     */
    @Override
    public BoxFileChunk selectByIdentifierAndChunkNumberAndCreateUser(String identifier,Integer chunkNumber,Long createUser){
        return boxFileChunkMapper.selectByIdentifierAndChunkNumberAndCreateUser(identifier,chunkNumber,createUser);
    }

    /**
     * 根据 IdentifierAndChunkNumberAndCreateUser 修改
     */
    @Override
    public Integer updateByIdentifierAndChunkNumberAndCreateUser(BoxFileChunk bean,String identifier,Integer chunkNumber,Long createUser){
        BoxFileChunkQuery query = new BoxFileChunkQuery();
        query.setIdentifier(identifier);
        query.setChunkNumber(chunkNumber);
        query.setCreateUser(createUser);
        return boxFileChunkMapper.updateByParam(bean,query);
    }


    /**
     * 根据 IdentifierAndChunkNumberAndCreateUser 删除
     */
    @Override
    public Integer deleteByIdentifierAndChunkNumberAndCreateUser(String identifier,Integer chunkNumber,Long createUser){
        return boxFileChunkMapper.deleteByIdentifierAndChunkNumberAndCreateUser(identifier,chunkNumber,createUser);
    }
}