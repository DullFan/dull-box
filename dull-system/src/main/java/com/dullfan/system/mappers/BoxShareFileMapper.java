package com.dullfan.system.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dullfan.system.entity.po.BoxShareFile;
import com.dullfan.system.entity.query.BoxShareFileQuery;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author DullFan
 * @date 2024-07-15 11:25:06
 */
@Mapper
public interface BoxShareFileMapper extends ABaseMapper<BoxShareFile,BoxShareFileQuery> {

    /**
    * 根据 Id 查询
    */
    BoxShareFile selectById(@Param("id")Long id);

    /**
    * 根据 Id 删除
    */
    Integer deleteById(@Param("id")Long id);


    /**
    * 根据 Id 批量删除
    */
    Integer deleteByIdBatch(@Param("list") List<Integer> list);
    /**
    * 根据 ShareIdAndFileId 查询
    */
    BoxShareFile selectByShareIdAndFileId(@Param("shareId")Long shareId,@Param("fileId")Long fileId);

    /**
    * 根据 ShareIdAndFileId 删除
    */
    Integer deleteByShareIdAndFileId(@Param("shareId")Long shareId,@Param("fileId")Long fileId);


    Integer deleteByShareIds(@Param("shareId")List<Long> shareIds, @Param("userId")Long userId);

    /**
     * 根据参数查询所有
     */
    List<BoxShareFile> selectAllList(@Param("query") BoxShareFileQuery query);

    List<BoxShareFile> selectByFileIds(@Param("list") List<Long> allAvailableFileIdList);

    @Override
    default BoxShareFile selectByIdCache(Long id) {
        return this.selectById(id);
    }

    @Override
    default Integer updateByIdCache(BoxShareFile boxShareFile, Long id) {
        BoxShareFileQuery boxShareFileQuery = new BoxShareFileQuery();
        boxShareFileQuery.setId(id);
        return this.updateByParam(boxShareFile, boxShareFileQuery);
    }

    @Override
    default Integer deleteByIdCache(Long id) {
        return this.deleteById(id);
    }
}

