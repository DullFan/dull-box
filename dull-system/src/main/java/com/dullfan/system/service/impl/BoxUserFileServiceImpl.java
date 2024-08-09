package com.dullfan.system.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.dullfan.common.constant.Constants;
import com.dullfan.common.entity.query.SimplePage;
import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.common.enums.DelFlagEnum;
import com.dullfan.common.enums.FolderFlagEnum;
import com.dullfan.common.enums.PageSizeEnum;
import com.dullfan.common.exception.ServiceException;
import com.dullfan.common.utils.SecurityUtils;
import com.dullfan.common.utils.encryption.IdUtil;
import com.dullfan.system.entity.po.*;
import com.dullfan.system.entity.query.BoxUserFileQuery;
import com.dullfan.system.entity.vo.BoxUserFileVO;
import com.dullfan.system.event.file.DeleteFileEvent;
import com.dullfan.system.event.file.FileRestoreEvent;
import com.dullfan.system.mappers.BoxUserFileMapper;
import com.dullfan.system.service.BoxUserFileService;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service("BoxUserFileService")
public class BoxUserFileServiceImpl implements BoxUserFileService {

    @Resource
    BoxUserFileMapper boxUserFileMapper;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<BoxUserFile> selectListByParam(BoxUserFileQuery param) {
        return boxUserFileMapper.selectList(param);
    }

    /**
     * 根据条件查询列表数量
     */
    @Override
    public Integer selectCountByParam(BoxUserFileQuery param) {
        return boxUserFileMapper.selectCount(param);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVo<BoxUserFile> selectListByPage(BoxUserFileQuery param) {
        int count = selectCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNum(), count, pageSize);
        param.setSimplePage(page);
        List<BoxUserFile> list = selectListByParam(param);
        return new PaginationResultVo<>(count, page.getPageSize(), page.getPageNum(), page.getPageTotal(), list);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer insertBatch(List<BoxUserFile> listBean) {
        return boxUserFileMapper.insertBatch(listBean);
    }

    /**
     * 根据 FileId 查询
     */
    @Override
    public BoxUserFile selectByFileId(Long fileId) {
        return boxUserFileMapper.selectByFileId(fileId);
    }

    @Override
    public List<BoxUserFile> selectByFileIds(List<Long> fileId, Long userId) {
        return boxUserFileMapper.selectByIds(fileId, userId);
    }

    @Override
    public List<BoxUserFileVO> selectFileList(Long parentId,
                                              List<Long> fileTypeArray,
                                              Long userId,
                                              Integer delFlag,
                                              List<Long> fileIdList) {
        return boxUserFileMapper.selectFileList(parentId,
                fileTypeArray,
                userId,
                delFlag,
                fileIdList
        );
    }

    /**
     * 根据 FileId 修改
     */
    @Override
    public Integer updateByFileId(BoxUserFile bean, Long fileId) {
        BoxUserFileQuery query = new BoxUserFileQuery();
        query.setFileId(fileId);
        return boxUserFileMapper.updateByParam(bean, query);
    }

    /**
     * 根据 FileId 逻辑删除
     */
    @Override
    public Integer softDeleteByFileId(DeleteFilePO deleteFilePO) {
        List<Long> ids = checkFileDeleteCondition(deleteFilePO.getFileIds());
        Integer i = boxUserFileMapper.updateDelFlag(ids, DelFlagEnum.YES.getCode(), deleteFilePO.getUserId());
        afterFileDelete(ids);
        return i;
    }

    /**
     * 文件删除的后置操作
     */
    private void afterFileDelete(List<Long> ids) {
        DeleteFileEvent deleteFileEvent = new DeleteFileEvent(this, ids);
        applicationContext.publishEvent(deleteFileEvent);
    }

    /**
     * 根据 FileId 逻辑删除
     */
    @Override
    public void restoreFileByFileId(RestorePO restorePO) {
        List<BoxUserFile> records = new ArrayList<>();
        checkFileRestoreCondition(restorePO.getFileIds(), records, restorePO.getUserId());
        checkRestoreFilename(records);
        doRestore(records);
        afterRestore(records);
    }

    /**
     * 文件还原后置操作
     */
    private void afterRestore(List<BoxUserFile> records) {
        FileRestoreEvent fileRestoreEvent = new FileRestoreEvent(this, records.stream().map(BoxUserFile::getFileId).toList());
        applicationContext.publishEvent(fileRestoreEvent);
    }

    /**
     * 执行文件还原
     */
    private void doRestore(List<BoxUserFile> records) {
        records.forEach(record -> {
            record.setDelFlag(DelFlagEnum.NO.getCode());
            record.setUpdateTime(new Date());
            record.setUpdateUser(record.getUserId());
        });
        boxUserFileMapper.updateByFileIds(records);
    }

    /**
     * 检查还原名称是否能够使用
     */
    private void checkRestoreFilename(List<BoxUserFile> records) {
        Set<String> filenameSet = records.stream().map(record -> record.getFilename() + Constants.COMMON_SEPARATOR + record.getParentId()).collect(Collectors.toSet());
        if (filenameSet.size() != records.size()) {
            throw new ServiceException("文件还原失败,该还原文件中存在同名文件,请逐个还原并重命名");
        }
        for (BoxUserFile record : records) {
            BoxUserFileQuery boxUserFileQuery = new BoxUserFileQuery();
            boxUserFileQuery.setUserId(record.getUserId());
            boxUserFileQuery.setParentId(record.getParentId());
            boxUserFileQuery.setFilename(record.getFilename());
            boxUserFileQuery.setDelFlag(DelFlagEnum.NO.getCode());
            Integer count = boxUserFileMapper.selectCount(boxUserFileQuery);
            if (count > 0) {
                throw new ServiceException("文件" + record.getFilename() + "还原失败,该还原文件中存在同名文件或文件夹,请重命名后在还原操作");
            }
        }
    }

    /**
     * 检查还原文件是否合法
     */
    private void checkFileRestoreCondition(List<String> fileIds, List<BoxUserFile> records, Long userId) {
        List<Long> collect = fileIds.stream().map(IdUtil::decrypt).toList();
        List<BoxUserFile> boxUserFiles = boxUserFileMapper.selectByIds(collect, userId);
        if (CollectionUtils.isEmpty(boxUserFiles)) {
            throw new ServiceException("文件还原失败");
        }
        if (boxUserFiles.size() != fileIds.size()) {
            throw new ServiceException("存在不合法的文件记录");
        }
        records.addAll(new HashSet<>(boxUserFiles).stream().toList());
    }

    /**
     * 根据 FileId 删除
     */
    @Override
    public Integer deleteByFileId(Long fileId) {
        return boxUserFileMapper.deleteByFileId(fileId);
    }

    @Override
    public Integer updateByFileIds(List<BoxUserFile> prepareRecords) {
        return boxUserFileMapper.updateByFileIds(prepareRecords);
    }

    @Override
    public Integer deleteByFileIds(List<Long> fileIdList) {
        return boxUserFileMapper.deleteByFileIds(fileIdList);
    }

    /**
     * 递归查询所有的子文件信息
     */
    @Override
    public List<BoxUserFile> findAllFileRecords(List<BoxUserFile> records) {
        List<BoxUserFile> result = Lists.newArrayList(records);
        if (CollectionUtils.isEmpty(records)) {
            return result;
        }
        long folderCount = result.stream().filter(record -> record.getFolderFlag().equals(FolderFlagEnum.YES.getCode())).count();
        if (folderCount == 0) {
            return result;
        }
        List<BoxUserFile> folderChildList = Lists.newArrayList();
        result.forEach(record -> doFindAllChildRecords(folderChildList, record));
        result.addAll(folderChildList);
        return result;
    }
    /**
     * 递归查询所有的子文件信息
     */
    @Override
    public List<BoxUserFile> findAllFileRecordsByFileId(List<Long> fileIdList) {
        if(CollectionUtils.isEmpty(fileIdList)){
            return Lists.newArrayList();
        }
        List<BoxUserFile> records = selectByFileIds(fileIdList, null);
        if(CollectionUtils.isEmpty(records)){
            return Lists.newArrayList();
        }
        return findAllFileRecords(records);
    }

    @Override
    public List<BoxUserFile> recycles() {
        return boxUserFileMapper.recycles(SecurityUtils.getUserId());
    }

    /**
     * 递归查询所有子文件列表
     */
    private void doFindAllChildRecords(List<BoxUserFile> result, BoxUserFile record) {
        if (Objects.isNull(record)) {
            return;
        }
        if (record.getFolderFlag().equals(FolderFlagEnum.NO.getCode())) {
            return;
        }
        List<BoxUserFile> childRecords = findChildRecordsIgnoreDelFlag(record.getFileId());
        if (CollectionUtils.isEmpty(childRecords)) {
            return;
        }
        result.addAll(childRecords);
        childRecords.stream()
                .filter(item -> FolderFlagEnum.YES.getCode().equals(item.getFolderFlag()))
                .forEach(item -> doFindAllChildRecords(result, item));
    }

    /**
     * 查询文件夹下面的文件记录,忽略删除标识
     */
    private List<BoxUserFile> findChildRecordsIgnoreDelFlag(Long fileId) {
        BoxUserFileQuery boxUserFileQuery = new BoxUserFileQuery();
        boxUserFileQuery.setParentId(fileId);
        return boxUserFileMapper.selectAllList(boxUserFileQuery);
    }

    private List<Long> checkFileDeleteCondition(List<String> fileIds) {
        List<Long> collect = fileIds.stream().map(IdUtil::decrypt).toList();
        List<BoxUserFile> boxUserFiles = boxUserFileMapper.selectByIds(collect, SecurityUtils.getUserId());
        if (boxUserFiles.size() != fileIds.size()) {
            throw new ServiceException("存在不合法的文件记录");
        }
        return new HashSet<>(collect).stream().toList();
    }


}