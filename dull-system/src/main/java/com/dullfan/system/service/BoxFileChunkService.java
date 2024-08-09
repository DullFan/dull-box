package com.dullfan.system.service;

import java.util.List;

import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.system.entity.po.BoxFileChunk;
import com.dullfan.system.entity.query.BoxFileChunkQuery;

public interface BoxFileChunkService {

    /**
     * 根据条件查询列表
     */
    List<BoxFileChunk> selectListByParam(BoxFileChunkQuery param);

    /**
     * 根据条件查询列表
     */
    List<BoxFileChunk> selectListByParamNoPage(BoxFileChunkQuery param);

    /**
     * 根据条件查询列表
     */
    List<BoxFileChunk> selectCleanChunkList(BoxFileChunkQuery param);


    /**
     * 根据条件查询列表数量
     */
    Integer selectCountByParam(BoxFileChunkQuery param);

    /**
     * 分页查询
     */
    PaginationResultVo<BoxFileChunk> selectListByPage(BoxFileChunkQuery param);

    /**
     * 新增
     */
    Integer insert(BoxFileChunk bean);

    /**
     * 批量新增
     */
    Integer insertBatch(List<BoxFileChunk> listBean);

    /**
     * 根据 Id 查询
     */
     BoxFileChunk selectById(Long id);

    /**
     * 根据 Id 修改
     */
    Integer updateById(BoxFileChunk bean,Long id);


    /**
     * 根据 Id 删除
     */
    Integer deleteById(Long id);

    /**
     * 根据 Id 批量删除
     */
    Integer deleteByIdBatch(List<Long> list);
    /**
     * 根据 IdentifierAndChunkNumberAndCreateUser 查询
     */
     BoxFileChunk selectByIdentifierAndChunkNumberAndCreateUser(String identifier,Integer chunkNumber,Long createUser);

    /**
     * 根据 IdentifierAndChunkNumberAndCreateUser 修改
     */
    Integer updateByIdentifierAndChunkNumberAndCreateUser(BoxFileChunk bean,String identifier,Integer chunkNumber,Long createUser);


    /**
     * 根据 IdentifierAndChunkNumberAndCreateUser 删除
     */
    Integer deleteByIdentifierAndChunkNumberAndCreateUser(String identifier,Integer chunkNumber,Long createUser);

}