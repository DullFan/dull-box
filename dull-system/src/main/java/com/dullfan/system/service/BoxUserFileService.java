package com.dullfan.system.service;

import java.util.List;

import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.system.entity.dto.BoxUserFileDto;
import com.dullfan.system.entity.po.*;
import com.dullfan.system.entity.query.BoxUserFileQuery;
import com.dullfan.system.entity.vo.BoxUserFileVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BoxUserFileService {

    /**
     * 根据条件查询列表
     */
    List<BoxUserFile> selectListByParam(BoxUserFileQuery param);

    /**
     * 根据条件查询列表数量
     */
    Integer selectCountByParam(BoxUserFileQuery param);

    /**
     * 分页查询
     */
    PaginationResultVo<BoxUserFile> selectListByPage(BoxUserFileQuery param);

    /**
     * 批量新增
     */
    Integer insertBatch(List<BoxUserFile> listBean);

    /**
     * 根据 FileId 查询
     */
     BoxUserFile selectByFileId(Long fileId);

    /**
     * 根据 FileId 批量查询
     */
    List<BoxUserFile> selectByFileIds(List<Long> fileId,Long userId);

     List<BoxUserFileVO> selectFileList(Long parentId,
                                              List<Long> fileTypeArray,
                                              Long userId,
                                              Integer delFlag,
                                              List<Long> fileIdList);

    /**
     * 根据 FileId 修改
     */
    Integer updateByFileId(BoxUserFile bean,Long fileId);

    /**
     * 根据 FileId 逻辑删除
     */
    Integer softDeleteByFileId(DeleteFilePO deleteFilePO);

    /**
     * 根据 FileId 还原文件
     */
    void restoreFileByFileId(RestorePO restorePO);
    /**
     * 根据 FileId 删除
     */
    Integer deleteByFileId(Long fileId);


    Integer updateByFileIds(List<BoxUserFile> prepareRecords);

    Integer deleteByFileIds(List<Long> fileIdList);

    /**
     * 递归查询子文件信息
     */
    List<BoxUserFile> findAllFileRecords(List<BoxUserFile> records);

    /**
     * 递归查询子文件信息
     */
    List<BoxUserFile> findAllFileRecordsByFileId(List<Long> fileIdList);

    /**
     * 获取回收站列表
     */
    List<BoxUserFile> recycles();
}