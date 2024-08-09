package com.dullfan.system.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dullfan.system.entity.po.BoxFileChunk;
import com.dullfan.system.entity.query.BoxFileChunkQuery;

/**
 * @author DullFan
 * @date 2024-07-15 11:25:06
 */
@Mapper
public interface BoxFileChunkMapper extends ABaseMapper<BoxFileChunk,BoxFileChunkQuery> {

    /**
    * 根据 Id 查询
    */
    BoxFileChunk selectById(@Param("id")Long id);

    /**
    * 根据 Id 删除
    */
    Integer deleteById(@Param("id")Long id);


    /**
    * 根据 Id 批量删除
    */
    Integer deleteByIdBatch(@Param("list") List<Long> list);
    /**
    * 根据 IdentifierAndChunkNumberAndCreateUser 查询
    */
    BoxFileChunk selectByIdentifierAndChunkNumberAndCreateUser(@Param("identifier")String identifier,@Param("chunkNumber")Integer chunkNumber,@Param("createUser")Long createUser);

    /**
    * 根据 IdentifierAndChunkNumberAndCreateUser 删除
    */
    Integer deleteByIdentifierAndChunkNumberAndCreateUser(@Param("identifier")String identifier,@Param("chunkNumber")Integer chunkNumber,@Param("createUser")Long createUser);


    List<BoxFileChunk> selectListNoPage(@Param("query") BoxFileChunkQuery param);

    List<BoxFileChunk> selectCleanChunkList(@Param("query") BoxFileChunkQuery param);


    @Override
    default BoxFileChunk selectByIdCache(Long id) {
        return this.selectById(id);
    }

    @Override
    default Integer updateByIdCache(BoxFileChunk boxFileChunk, Long id) {
        BoxFileChunkQuery boxFileChunkQuery = new BoxFileChunkQuery();
        boxFileChunkQuery.setId(id);
        return this.updateByParam(boxFileChunk, boxFileChunkQuery);
    }

    @Override
    default Integer deleteByIdCache(Long id) {
        return this.deleteById(id);
    }
}

