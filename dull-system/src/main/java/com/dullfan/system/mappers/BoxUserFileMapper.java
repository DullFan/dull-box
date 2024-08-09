package com.dullfan.system.mappers;

import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.system.entity.dto.BoxUserFileDto;
import com.dullfan.system.entity.dto.FileSearchDto;
import com.dullfan.system.entity.vo.BoxUserFileVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import com.dullfan.system.entity.po.BoxUserFile;
import com.dullfan.system.entity.query.BoxUserFileQuery;

/**
 * @author DullFan
 * @date 2024-07-15 11:25:06
 */
@Mapper
public interface BoxUserFileMapper extends ABaseMapper<BoxUserFile, BoxUserFileQuery> {

    /**
     * 根据 FileId 查询
     */
    BoxUserFile selectByFileId(@Param("fileId") Long fileId);

    /**
     * 根据 FileId 删除
     */
    Integer deleteByFileId(@Param("fileId") Long fileId);

    /**
     * 根据Dto内容查询列表
     */
    List<BoxUserFile> selectListByDto(@Param("param") BoxUserFileDto boxUserFileDto);

    /**
     * 批量查询
     */
    List<BoxUserFile> selectByIds(@Param("list") List<Long> collect, @Param("user_id") Long userId);

    List<BoxUserFileVO> selectFileList(
            @Param("parentId") Long parentId,
            @Param("fileTypeArray") List<Long> fileTypeArray,
            @Param("userId") Long userId,
            @Param("delFlag") Integer delFlag,
            @Param("fileIdList") List<Long> fileIdList
    );

    Integer updateDelFlag(@Param("ids") List<Long> ids, @Param("del_flag") Integer code, @Param("user_id") Long userId);

    /**
     * 查询所有数据
     */
    List<BoxUserFile> selectAllList(@Param("query") BoxUserFileQuery boxUserFileQuery);

    /**
     * 批量更新
     */
    Integer updateByFileIds(@Param("list") List<BoxUserFile> prepareRecords);

    /**
     * 文件搜索
     */
    List<BoxUserFileVO> searchFile(@Param("param") FileSearchDto fileSearchDto);

    Integer deleteByFileIds(@Param("ids") List<Long> fileIdList);

    @Override
    default BoxUserFile selectByIdCache(Long id) {
        return this.selectByFileId(id);
    }

    @Override
    default Integer updateByIdCache(BoxUserFile boxUserFile, Long id) {
        BoxUserFileQuery boxUserFileQuery = new BoxUserFileQuery();
        boxUserFileQuery.setFileId(id);
        return this.updateByParam(boxUserFile,boxUserFileQuery);
    }

    @Override
    default Integer deleteByIdCache(Long id) {
        return this.deleteByFileId(id);
    }

    List<BoxUserFile> recycles(@Param("userId")Long userId);
}


