package com.dullfan.system.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dullfan.system.entity.po.BoxFile;
import com.dullfan.system.entity.query.BoxFileQuery;

/**
 * @author DullFan
 * @date 2024-07-15 11:25:06
 */
@Mapper
public interface BoxFileMapper extends ABaseMapper<BoxFile,BoxFileQuery> {

    /**
    * 根据 FileId 查询
    */
    BoxFile selectByFileId(@Param("fileId")Long fileId);

    /**
    * 根据 FileId 删除
    */
    Integer deleteByFileId(@Param("fileId")Long fileId);


    Integer deleteByFileIds(@Param("ids") List<Long> fileIds);

    List<BoxFile> selectByFileIds(@Param("ids") List<Long> fileIds);

    @Override
    default BoxFile selectByIdCache(Long id) {
        return this.selectByFileId(id);
    }

    @Override
    default Integer updateByIdCache(BoxFile boxFile, Long id) {
        BoxFileQuery boxFileQuery = new BoxFileQuery();
        boxFileQuery.setFileId(id);
        return this.updateByParam(boxFile, boxFileQuery);
    }

    @Override
    default Integer deleteByIdCache(Long id) {
        return this.deleteByFileId(id);
    }
}

