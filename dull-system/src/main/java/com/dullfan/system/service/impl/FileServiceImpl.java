package com.dullfan.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.dullfan.common.constant.Constants;
import com.dullfan.common.constant.FileConstants;
import com.dullfan.common.entity.po.DeleteFile;
import com.dullfan.common.entity.po.MergeFile;
import com.dullfan.common.entity.po.StoreFile;
import com.dullfan.common.enums.DelFlagEnum;
import com.dullfan.common.enums.FileTypeEnum;
import com.dullfan.common.enums.FolderFlagEnum;
import com.dullfan.common.enums.ReadFile;
import com.dullfan.system.entity.vo.*;
import com.dullfan.system.event.search.UserSearchEvent;
import com.dullfan.common.exception.ServiceException;
import com.dullfan.common.storage.StorageEngine;
import com.dullfan.common.utils.DateUtils;
import com.dullfan.common.utils.SecurityUtils;
import com.dullfan.common.utils.StringUtils;
import com.dullfan.common.utils.encryption.FileUtils;
import com.dullfan.common.utils.encryption.IdUtil;
import com.dullfan.common.utils.http.HttpUtil;
import com.dullfan.system.entity.dto.*;
import com.dullfan.system.entity.po.*;
import com.dullfan.system.entity.query.BoxFileChunkQuery;
import com.dullfan.system.entity.query.BoxFileQuery;
import com.dullfan.system.entity.query.BoxUserFileQuery;
import com.dullfan.system.mappers.BoxFileMapper;
import com.dullfan.system.mappers.BoxUserFileMapper;
import com.dullfan.system.service.BoxUserFileService;
import com.dullfan.system.service.FileChunkService;
import com.dullfan.system.service.FileService;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import static com.dullfan.common.enums.FileTypeEnum.AUDIO_FILE;
import static com.dullfan.common.enums.FileTypeEnum.VIDEO_FILE;

@Service("FileServiceImpl")
public class FileServiceImpl implements FileService {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private BoxUserFileMapper boxUserFileMapper;

    @Resource
    private BoxUserFileService boxUserFileService;

    @Resource
    private BoxFileMapper boxFileMapper;

    @Resource
    private StorageEngine storageEngine;

    @Resource
    private FileChunkService fileChunkService;

    /**
     * 新增
     */
    @Override
    public Long insert(BoxUserFile bean) {
        return saveUserFile(bean.getParentId(), bean.getFilename(), FolderFlagEnum.YES, null, null, bean.getUserId(), null);
    }

    @Override
    public List<BoxUserFile> selectList(BoxUserFileDto boxUserFileDto) {
        System.out.println(JSONObject.toJSONString(boxUserFileDto.getFileTypeArray()));
        return boxUserFileMapper.selectListByDto(boxUserFileDto);
    }

    /**
     * 创建文件夹
     */
    @Override
    public Long createFolder(CreateFolderPO createFolderPO) {
        Long decryptParentId = IdUtil.decrypt(createFolderPO.getParentId());
        return saveUserFile(decryptParentId, createFolderPO.getFileName(), FolderFlagEnum.YES, null, null, createFolderPO.getUserId(), null);
    }

    /**
     * 更新文件名称
     */
    @Override
    public Integer updateFilename(UpdateFilenamePO updateFilenamePO) {
        checkUpdateFilenameCondition(updateFilenamePO);
        BoxUserFile boxUserFile = new BoxUserFile();
        boxUserFile.setFilename(updateFilenamePO.getNewFilename());
        boxUserFile.setUpdateTime(DateUtils.getNowDate());
        boxUserFile.setUpdateUser(updateFilenamePO.getUserId());
        return boxUserFileService.updateByFileId(boxUserFile, IdUtil.decrypt(updateFilenamePO.getFileId()));
    }

    /**
     * 文件秒传
     */
    @Override
    public boolean secUpload(SecUploadFilePO secUploadFilePO) {
        BoxFile boxFile = getFileByUserIdAndIdentifier(SecurityUtils.getUserId(), secUploadFilePO.getIdentifier());
        if (Objects.isNull(boxFile)) {
            return false;
        }
        saveUserFile(IdUtil.decrypt(secUploadFilePO.getParentId()), secUploadFilePO.getFilename(), FolderFlagEnum.NO, FileTypeEnum.getFileTypeCode(FileUtils.getFileSuffix(secUploadFilePO.getFilename())), boxFile.getFileId(), SecurityUtils.getUserId(), boxFile.getFileSizeDesc());
        return true;
    }

    /**
     * 单文件上传
     */
    @Override
    @Transactional
    public void upload(FileUploadPO fileUploadPO) {
        // 初始化dto数据
        FileUploadDto fileUploadDto = new FileUploadDto();
        BeanUtils.copyProperties(fileUploadPO, fileUploadDto);
        fileUploadDto.setFilename(fileUploadPO.getFile().getOriginalFilename());
        fileUploadDto.setTotalSize(fileUploadPO.getFile().getSize());

        // 先验证用户已经上传该文件
        SecUploadFilePO secUploadFilePO = new SecUploadFilePO();
        secUploadFilePO.setParentId(fileUploadDto.getParentId());
        secUploadFilePO.setFilename(fileUploadDto.getFilename());
        secUploadFilePO.setIdentifier(fileUploadDto.getIdentifier());
        boolean flag = secUpload(secUploadFilePO);

        if (!flag) {
            saveFile(fileUploadDto);
            saveUserFile(IdUtil.decrypt(fileUploadPO.getParentId()), fileUploadDto.getFilename(), FolderFlagEnum.NO, FileTypeEnum.getFileTypeCode(FileUtils.getFileSuffix(fileUploadDto.getFilename())), fileUploadDto.getRecord().getFileId(), SecurityUtils.getUserId(), fileUploadDto.getRecord().getFileSizeDesc());
        }
    }

    /**
     * 文件分片上传
     */
    @Override
    public FileChunkUploadVO chunkUpload(FileChunkUploadPO fileChunkUploadPO) {
        FileChunkUploadDto fileChunkUploadDto = new FileChunkUploadDto();
        fileChunkUploadDto.setCurrentChunkSize(fileChunkUploadPO.getFile().getSize());
        BeanUtils.copyProperties(fileChunkUploadPO, fileChunkUploadDto);
        fileChunkService.saveChunkFile(fileChunkUploadDto);
        FileChunkUploadVO fileChunkUploadVO = new FileChunkUploadVO();
        fileChunkUploadVO.setMergeFlag(fileChunkUploadDto.getMergeFlagEnum().getCode());
        return fileChunkUploadVO;
    }

    /**
     * 查询用户已上传的文件列表
     */
    @Override
    public UploadedChunksVO getUploadedChunks(QueryUploadedChunksPO queryUploadedChunksPO) {
        return fileChunkService.checkUploadChunks(queryUploadedChunksPO.getIdentifier());
    }

    /**
     * 文件分片合并
     */
    @Override
    public void mergeFile(FileChunkMergePO fileChunkMergePO) {
        FileChunkMergeDto fileChunkMergeDto = new FileChunkMergeDto();
        BeanUtils.copyProperties(fileChunkMergePO, fileChunkMergeDto);
        fileChunkMergeDto.setParentId(IdUtil.decrypt(fileChunkMergePO.getParentId()));
        mergeFileChunkAndSaveFile(fileChunkMergeDto);
        saveUserFile(fileChunkMergeDto.getParentId(), fileChunkMergeDto.getFilename(), FolderFlagEnum.NO, FileTypeEnum.getFileTypeCode(FileUtils.getFileSuffix(fileChunkMergeDto.getFilename())), fileChunkMergeDto.getRecord().getFileId(), SecurityUtils.getUserId(), fileChunkMergeDto.getRecord().getFileSizeDesc());
    }

    /**
     * 文件下载
     */
    @Override
    public void download(FileDownload fileDownload) {
        BoxUserFile record = boxUserFileMapper.selectByFileId(fileDownload.getFileId());
        checkOperatePermission(record, fileDownload.getUserId());
        if (checkIsFolder(record)) {
            throw new ServiceException("文件夹暂不支持下载");
        }
        doDownload(record, fileDownload.getResponse());
    }

    /**
     * 文件下载,不检查用户权限
     */
    @Override
    public void downloadWithoutCheckUser(FileDownload fileDownload) {
        BoxUserFile record = boxUserFileMapper.selectByFileId(fileDownload.getFileId());
        if (Objects.isNull(record)) {
            throw new ServiceException("当前文件记录不存在");
        }

        if (checkIsFolder(record)) {
            throw new ServiceException("文件夹暂不支持下载");
        }
        doDownload(record, fileDownload.getResponse());
    }

    /**
     * 文件预览
     */
    @Override
    public void preview(FilePreview filePreview) {
        BoxUserFile record = boxUserFileMapper.selectByFileId(filePreview.getFileId());
        checkOperatePermission(record, filePreview.getUserId());
        if (checkIsFolder(record)) {
            throw new ServiceException("文件夹暂不支持下载");
        }
        doPreview(record, filePreview.getResponse());
    }

    /**
     * 文件预览,不检查用户权限
     */
    @Override
    public void previewWithoutCheckUser(FilePreview filePreview) {
        BoxUserFile record = boxUserFileMapper.selectByFileId(filePreview.getFileId());
        if (Objects.isNull(record)) {
            throw new ServiceException("当前文件记录不存在");
        }

        if (checkIsFolder(record)) {
            throw new ServiceException("文件夹暂不支持下载");
        }
        doPreview(record, filePreview.getResponse());
    }

    @Override
    public List<FolderTreeNodeVO> getFolderTree() {
        List<BoxUserFile> folderRecords = queryFolderRecords();
        return assembleFolderTreeNodeVoList(folderRecords);
    }

    /**
     * 文件转移
     */
    @Override
    public void transfer(TransferFileDto transferFileDto) {
        checkTransferCondition(transferFileDto);
        doTransfer(transferFileDto);
    }

    /**
     * 文件复制
     */
    @Override
    public void copy(CopyFileDto copyFileDto) {
        checkCopyCondition(copyFileDto);
        doCopy(copyFileDto);
    }

    /**
     * 文件搜索
     */
    @Override
    public List<BoxUserFileVO> search(FileSearchDto fileSearchDto) {
        List<BoxUserFileVO> result = doSearch(fileSearchDto);
        fileParentFilename(result);
        afterSearch(fileSearchDto);
        return result;
    }

    @Override
    public List<BreadcrumbVO> getBreadcrumbs(Long fileId) {
        List<BoxUserFile> folderRecords = queryFolderRecords();
        Map<Long, BreadcrumbVO> prepareBreadcrumbVoMap = folderRecords.stream().map(BreadcrumbVO::transfer).collect(Collectors.toMap(BreadcrumbVO::getId, a -> a));
        BreadcrumbVO currentNode;
        List<BreadcrumbVO> result = Lists.newArrayList();
        do {
            currentNode = prepareBreadcrumbVoMap.get(fileId);
            if (Objects.nonNull(currentNode)) {
                result.add(0, currentNode);
                fileId = currentNode.getParentId();
            }
        } while (Objects.nonNull(currentNode));
        return result;
    }

    /**
     * 搜索后的操作
     */
    private void afterSearch(FileSearchDto fileSearchDto) {
        UserSearchEvent userSearchEvent = new UserSearchEvent(this, fileSearchDto.getKeyword(), fileSearchDto.getUserId());
        applicationContext.publishEvent(userSearchEvent);
    }

    /**
     * 获取文件父文件夹名称
     */
    private void fileParentFilename(List<BoxUserFileVO> result) {
        if (CollectionUtils.isEmpty(result)) {
            return;
        }
        List<Long> parentIdList = result.stream().map(BoxUserFileVO::getParentId).toList();
        List<BoxUserFile> parentRecords = boxUserFileMapper.selectByIds(parentIdList,SecurityUtils.getUserId());
        Map<Long, String> fileID2filenameMap = parentRecords.stream().collect(Collectors.toMap(BoxUserFile::getFileId, BoxUserFile::getFilename));
        result.forEach(vo -> vo.setParentFilename(fileID2filenameMap.get(vo.getParentId())));
    }

    /**
     * 搜索文件列表
     */
    private List<BoxUserFileVO> doSearch(FileSearchDto fileSearchDto) {
        return boxUserFileMapper.searchFile(fileSearchDto);
    }

    /**
     * 执行文件复制的动作
     */
    private void doCopy(CopyFileDto copyFileDto) {
        List<BoxUserFile> prepareRecords = copyFileDto.getPrepareRecords();
        if (CollectionUtils.isNotEmpty(prepareRecords)) {
            List<BoxUserFile> allRecords = Lists.newArrayList();
            prepareRecords.forEach(recode -> assembleCopyChildRecord(allRecords, recode, copyFileDto.getTargetParentId()));

            if (boxUserFileService.insertBatch(allRecords) == 0) {
                throw new ServiceException("文件复制失败");
            }
        }
    }

    /**
     * 拼装当前文件记录以及所有的子文件记录
     */
    private void assembleCopyChildRecord(List<BoxUserFile> allRecords, BoxUserFile recode, Long targetParentId) {
        Long newFileId = IdUtil.get();
        Long oldFileId = recode.getFileId();

        recode.setParentId(targetParentId);
        recode.setFileId(newFileId);
        recode.setUserId(SecurityUtils.getUserId());
        recode.setCreateTime(DateUtils.getNowDate());
        recode.setUpdateUser(SecurityUtils.getUserId());
        recode.setUpdateTime(DateUtils.getNowDate());
        handleDuplicateFilename(recode);
        allRecords.add(recode);

        // 判断当前是否是文件夹，如果是将文件夹中的文件也添加进来
        if (checkIsFolder(recode)) {
            BoxUserFileQuery boxUserFileQuery = new BoxUserFileQuery();
            boxUserFileQuery.setParentId(oldFileId);
            boxUserFileQuery.setDelFlag(DelFlagEnum.NO.getCode());
            List<BoxUserFile> childRecords = boxUserFileMapper.selectAllList(boxUserFileQuery);
            if (CollectionUtils.isEmpty(childRecords)) {
                return;
            }
            childRecords.forEach(childRecord -> assembleCopyChildRecord(allRecords, childRecord, newFileId));
        }

    }

    /**
     * 文件复制的条件校验
     */
    private void checkCopyCondition(CopyFileDto copyFileDto) {
        Long targetParentId = copyFileDto.getTargetParentId();
        if (!checkIsFolder(boxUserFileMapper.selectByFileId(targetParentId))) {
            throw new ServiceException("目标文件不是一个文件夹");
        }

        List<Long> fileIds = copyFileDto.getFileIds();
        List<BoxUserFile> prepareRecords = boxUserFileService.selectByFileIds(fileIds,copyFileDto.getUserId());
        copyFileDto.setPrepareRecords(prepareRecords);
        if (checkIsChildFolder(prepareRecords, targetParentId)) {
            throw new ServiceException("不能将文件移动到自身或其子目录下");
        }
    }

    /**
     * 执行文件转移的动作
     */
    private void doTransfer(TransferFileDto transferFileDto) {
        List<BoxUserFile> prepareRecords = transferFileDto.getPrepareRecords();
        prepareRecords.forEach(record -> {
            record.setParentId(transferFileDto.getTargetParentId());
            record.setUserId(SecurityUtils.getUserId());
            record.setCreateUser(SecurityUtils.getUserId());
            record.setUpdateUser(SecurityUtils.getUserId());
            record.setCreateTime(DateUtils.getNowDate());
            record.setUpdateTime(DateUtils.getNowDate());
            handleDuplicateFilename(record);
        });
        Integer i = boxUserFileService.updateByFileIds(prepareRecords);
        if (i == 0) {
            throw new ServiceException("文件转移失败");
        }
    }

    /**
     * 文件转移的条件校验
     */
    private void checkTransferCondition(TransferFileDto transferFileDto) {
        if (!SecurityUtils.isAdminOrLoginUser(SecurityUtils.getUserId())) {
            throw new ServiceException("没有该文件的操作权限");
        }

        Long targetParentId = transferFileDto.getTargetParentId();
        if (!checkIsFolder(boxUserFileMapper.selectByFileId(targetParentId))) {
            throw new ServiceException("目标文件不是一个文件夹");
        }

        List<Long> fileIds = transferFileDto.getFileIds();
        List<BoxUserFile> prepareRecords = boxUserFileService.selectByFileIds(fileIds,SecurityUtils.getUserId());
        transferFileDto.setPrepareRecords(prepareRecords);
        if (checkIsChildFolder(prepareRecords, targetParentId)) {
            throw new ServiceException("不能将文件移动到自身或其子目录下");
        }

    }

    /**
     * 验证选中的要转移的文件夹列表中不能含有目标文件夹以及其子文件夹
     */
    private boolean checkIsChildFolder(List<BoxUserFile> prepareRecords, Long targetParentId) {
        // 如果要操作的文件列表中没有文件夹，那就直接返回false
        prepareRecords = prepareRecords.stream().filter(record -> Objects.equals(record.getFolderFlag(), FolderFlagEnum.YES.getCode())).toList();

        if (CollectionUtils.isEmpty(prepareRecords)) {
            return false;
        }
        // 查询当前用户下的所有文件夹
        List<BoxUserFile> folderRecords = queryFolderRecords();
        // 按照父文件分类
        Map<Long, List<BoxUserFile>> folderRecordMap = folderRecords.stream().collect(Collectors.groupingBy(BoxUserFile::getParentId));
        List<BoxUserFile> unavailableFolderRecords = Lists.newArrayList();
        unavailableFolderRecords.addAll(prepareRecords);
        // 循环判断是否包含目标文件夹以及其子文件夹
        prepareRecords.forEach(record -> findAllChildFolderRecords(unavailableFolderRecords, folderRecordMap, record));

        List<Long> unavailableFolderRecordsIds = unavailableFolderRecords.stream().map(BoxUserFile::getFileId).toList();
        return unavailableFolderRecordsIds.contains(targetParentId);
    }

    /**
     * 查询文件夹的所有子文件夹记录
     */
    private void findAllChildFolderRecords(List<BoxUserFile> unavailableFolderRecords, Map<Long, List<BoxUserFile>> folderRecordMap, BoxUserFile record) {
        if (Objects.isNull(record)) {
            return;
        }
        List<BoxUserFile> childFolderRecords = folderRecordMap.get(record.getFileId());
        if (CollectionUtils.isEmpty(childFolderRecords)) {
            return;
        }
        unavailableFolderRecords.addAll(childFolderRecords);
        childFolderRecords.forEach(childRecord -> findAllChildFolderRecords(unavailableFolderRecords, folderRecordMap, record));
    }

    private List<FolderTreeNodeVO> assembleFolderTreeNodeVoList(List<BoxUserFile> folderRecords) {
        if (CollectionUtils.isEmpty(folderRecords)) {
            return Lists.newArrayList();
        }

        List<FolderTreeNodeVO> mappedFolderTreeNodeVOList = new ArrayList<>();
        for (BoxUserFile folderRecord : folderRecords) {
            FolderTreeNodeVO nodeVO = new FolderTreeNodeVO();
            nodeVO.setId(folderRecord.getFileId());
            nodeVO.setParentId(folderRecord.getParentId());
            nodeVO.setLabel(folderRecord.getFilename());
            // 初始化子节点列表
            nodeVO.setChildren(new ArrayList<>());
            mappedFolderTreeNodeVOList.add(nodeVO);
        }
        // 按照parentId分组
        Map<Long, List<FolderTreeNodeVO>> mappedFolderTreeNodeVOMap = mappedFolderTreeNodeVOList.stream().collect(Collectors.groupingBy(FolderTreeNodeVO::getParentId));
        // 循环查看文件ID是否是父文件，如果是将child添加进去
        for (FolderTreeNodeVO node : mappedFolderTreeNodeVOList) {
            List<FolderTreeNodeVO> children = mappedFolderTreeNodeVOMap.get(node.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                node.getChildren().addAll(children);
            }
        }

        return mappedFolderTreeNodeVOList.stream().filter(node -> Objects.equals(node.getParentId(), FileConstants.TOP_PARENT_ID)).toList();
    }

    /**
     * 查询用户所有有效的文件夹信息
     */
    private List<BoxUserFile> queryFolderRecords() {
        BoxUserFileQuery boxUserFileQuery = new BoxUserFileQuery();
        boxUserFileQuery.setUserId(SecurityUtils.getUserId());
        boxUserFileQuery.setFolderFlag(FolderFlagEnum.YES.getCode());
        boxUserFileQuery.setDelFlag(DelFlagEnum.NO.getCode());
        return boxUserFileMapper.selectAllList(boxUserFileQuery);
    }

    /**
     * 执行文件预览
     */
    private void doPreview(BoxUserFile record, HttpServletResponse response) {
        BoxFile boxFile = boxFileMapper.selectByFileId(record.getRealFileId());
        if (Objects.isNull(boxFile)) {
            throw new ServiceException("当前文件记录不存在");
        }
        addCommonResponseHeader(response, boxFile.getFilePreviewContentType(),boxFile.getFileSuffix());
        realFIle2OutputStream(boxFile.getRealPath(), response);
    }

    private void doDownload(BoxUserFile record, HttpServletResponse response) {
        BoxFile boxFile = boxFileMapper.selectByFileId(record.getRealFileId());
        if (Objects.isNull(boxFile)) {
            throw new ServiceException("当前文件记录不存在");
        }
        addCommonResponseHeader(response, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        addDownloadAttribute(response, record, boxFile);
        realFIle2OutputStream(boxFile.getRealPath(), response);
    }

    /**
     * 委托文件存储引擎去读取文件内容并写入到输出流中
     */
    private void realFIle2OutputStream(String realPath, HttpServletResponse response) {

        try {
            ReadFile readFile = new ReadFile();
            readFile.setRealPath(realPath);
            readFile.setOutputStream(response.getOutputStream());
            storageEngine.realFile(readFile);
        } catch (IOException e) {
            throw new ServiceException("文件下载失败");
        }
    }

    /**
     * 添加文件下载的属性信息
     */
    private void addDownloadAttribute(HttpServletResponse response, BoxUserFile record, BoxFile boxFile) {
        try {
            response.addHeader(FileConstants.CONTENT_DISPOSITION_STR, FileConstants.CONTENT_DISPOSITION_VALUE_PREFIX_STR + new String(record.getFilename().getBytes(FileConstants.GB2312_STR), FileConstants.IOS_8859_1_STR));
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("文件下载失败");
        }
        response.setContentLengthLong(Long.parseLong(boxFile.getFileSize()));
    }

    private void addCommonResponseHeader(HttpServletResponse response, String type) {
        response.reset();
        HttpUtil.addCorsResponseHeaders(response);
        response.addHeader(FileConstants.CONTENT_TYPE_STR, type);
        response.setContentType(type);

        response.setHeader("Accept-Ranges","bytes");
    }

    private void addCommonResponseHeader(HttpServletResponse response, String type,String fileSuffix) {
        response.reset();
        HttpUtil.addCorsResponseHeaders(response);
        response.addHeader(FileConstants.CONTENT_TYPE_STR, type);
        Integer fileTypeCode = FileTypeEnum.getFileTypeCode(fileSuffix);
        if(VIDEO_FILE.getCode().equals(fileTypeCode) || AUDIO_FILE.getCode().equals(fileTypeCode)){
            // 防止video标签无法拖动进度条
            response.setHeader("Accept-Ranges","bytes");
        }
        response.setContentType(type);

    }

    /**
     * 校验用户权限
     */
    private void checkOperatePermission(BoxUserFile record, Long userId) {
        if (Objects.isNull(record)) {
            throw new ServiceException("当前文件记录不存在");
        }

        if (!SecurityUtils.isAdminOrLoginUser(userId)) {
            throw new ServiceException("没有该文件的操作权限");
        }
    }

    /**
     * 验证是否是文件夹
     */
    private boolean checkIsFolder(BoxUserFile record) {
        return FolderFlagEnum.YES.getCode().equals(record.getFolderFlag());
    }

    /**
     * 合并文件分片并保存物理文件记录
     */
    private void mergeFileChunkAndSaveFile(FileChunkMergeDto fileChunkMergeDto) {
        doMergeFileChunk(fileChunkMergeDto);
        BoxFile record = doSaveFile(fileChunkMergeDto.getFilename(), fileChunkMergeDto.getRealPath(), fileChunkMergeDto.getTotalSize(), fileChunkMergeDto.getIdentifier(), SecurityUtils.getUserId());
        fileChunkMergeDto.setRecord(record);
    }

    /**
     * 委托文件存储引擎合并文件分片
     */
    private void doMergeFileChunk(FileChunkMergeDto fileChunkMergeDto) {
        BoxFileChunkQuery boxFileChunkQuery = new BoxFileChunkQuery();
        boxFileChunkQuery.setIdentifier(fileChunkMergeDto.getIdentifier());
        boxFileChunkQuery.setCreateUser(SecurityUtils.getUserId());
        boxFileChunkQuery.setExpirationTimeStart(new Date());
        List<BoxFileChunk> boxFileChunks = fileChunkService.selectList(boxFileChunkQuery);
        if (CollectionUtils.isEmpty(boxFileChunks)) {
            throw new ServiceException("该文件未找到分片记录");
        }
        List<String> realPathList = boxFileChunks.stream().sorted(Comparator.comparing(BoxFileChunk::getChunkNumber)).map(BoxFileChunk::getRealPath).toList();

        try {
            MergeFile mergeFile = new MergeFile();
            mergeFile.setFilename(fileChunkMergeDto.getFilename());
            mergeFile.setIdentifier(fileChunkMergeDto.getIdentifier());
            mergeFile.setUserId(SecurityUtils.getUserId());
            mergeFile.setTotalSize(fileChunkMergeDto.getTotalSize());
            mergeFile.setRealPathList(realPathList);
            storageEngine.mergeFile(mergeFile);
            fileChunkMergeDto.setRealPath(mergeFile.getRealPath());
            fileChunkMergeDto.setTotalSize(mergeFile.getTotalSize());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("文件分片合并失败");
        }


        // 删除文件分片信息
        List<Long> fileChunkRecordIdList = boxFileChunks.stream().map(BoxFileChunk::getId).toList();
        fileChunkService.removeByIds(fileChunkRecordIdList);
    }

    /**
     * 保存文件
     */
    private void saveFile(FileUploadDto fileUploadDto) {
        storeMultipartFile(fileUploadDto);
        BoxFile boxFile = doSaveFile(fileUploadDto.getFilename(), fileUploadDto.getRealPath(), fileUploadDto.getTotalSize(), fileUploadDto.getIdentifier(), SecurityUtils.getUserId());
        fileUploadDto.setRecord(boxFile);
    }

    /**
     * 保存实体文件记录
     */
    private BoxFile doSaveFile(String filename, String realPath, Long totalSize, String identifier, Long userId) {
        BoxFile boxFile = aseembleBoxFile(filename, realPath, totalSize, identifier, userId);
        if (!(boxFileMapper.insert(boxFile) >= 1)) {
            try {
                DeleteFile deleteFile = new DeleteFile();
                deleteFile.setRealFilePathList(Lists.newArrayList(realPath));
                storageEngine.delete(deleteFile);
            } catch (IOException e) {
                throw new ServiceException("doSaveFile文件删除失败");
            }
        }
        return boxFile;
    }

    /**
     * 拼装文件实体对象
     */
    private BoxFile aseembleBoxFile(String filename, String realPath, Long totalSize, String identifier, Long userId) {
        BoxFile boxFile = new BoxFile();
        boxFile.setFileId(IdUtil.get());
        boxFile.setFilename(filename);
        boxFile.setRealPath(realPath);
        boxFile.setCreateUser(userId);
        boxFile.setFileSize(String.valueOf(totalSize));
        boxFile.setFileSizeDesc(FileUtils.byteCountToDisplaySize(totalSize));
        boxFile.setFileSuffix(FileUtils.getFileSuffix(filename));
        boxFile.setIdentifier(identifier);
        boxFile.setCreateUser(SecurityUtils.getUserId());
        boxFile.setCreateTime(DateUtils.getNowDate());
//        boxFile.setFilePreviewContentType(DateUtils.getNowDate());
        return boxFile;
    }


    /**
     * 上传单文件
     * 该方法委托文件存储引擎实现
     */
    private void storeMultipartFile(FileUploadDto fileUploadDto) {
        try {
            StoreFile storeFile = new StoreFile();
            storeFile.setInputStream(fileUploadDto.getFile().getInputStream());
            storeFile.setFilename(fileUploadDto.getFilename());
            storeFile.setTotalSize(fileUploadDto.getTotalSize());
            storageEngine.store(storeFile);
            fileUploadDto.setRealPath(storeFile.getRealPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("文件上传失败");
        }

    }

    private BoxFile getFileByUserIdAndIdentifier(Long userId, String identifier) {
        BoxFileQuery boxFileQuery = new BoxFileQuery();
        boxFileQuery.setIdentifier(identifier);
        boxFileQuery.setCreateUser(userId);
        List<BoxFile> boxFiles = boxFileMapper.selectList(boxFileQuery);
        if (CollectionUtils.isEmpty(boxFiles)) {
            return null;
        }
        return boxFiles.get(0);
    }

    /**
     * 更新文件名称的条件校验
     */
    private void checkUpdateFilenameCondition(UpdateFilenamePO updateFilenamePO) {
        Long fileID = IdUtil.decrypt(updateFilenamePO.getFileId());
        BoxUserFile boxUserFile = boxUserFileService.selectByFileId(fileID);
        if (boxUserFile == null) {
            throw new ServiceException("该文件ID无效");
        }

        if (boxUserFile.getFilename().equals(updateFilenamePO.getNewFilename())) {
            throw new ServiceException("文件名称重复");
        }

        // 文件名同级别不能重复
        BoxUserFileQuery boxUserFileQuery = new BoxUserFileQuery();
        boxUserFileQuery.setParentId(fileID);
        boxUserFileQuery.setFilename(updateFilenamePO.getNewFilename());
        Integer count = boxUserFileService.selectCountByParam(boxUserFileQuery);
        if (count > 0) {
            throw new ServiceException("文件名称重复");
        }
    }

    /**
     * 保存用户文件的映射记录
     */
    private Long saveUserFile(Long parentId, String filename, FolderFlagEnum folderFlagEnum, Integer fileType, Long realFileId, Long userId, String fileSizeDesc) {
        BoxUserFile boxUserFile = assembleBoxUserFile(parentId, userId, filename, folderFlagEnum, fileType, realFileId, fileSizeDesc);
        if (boxUserFileMapper.insert(boxUserFile) == 0) {
            throw new ServiceException("保存文件信息失败");
        }
        return boxUserFile.getFileId();
    }

    private BoxUserFile assembleBoxUserFile(Long parentId, Long userId, String filename, FolderFlagEnum folderFlagEnum, Integer fileType, Long realFileId, String fileSizeDesc) {
        BoxUserFile boxUserFile = new BoxUserFile();
        boxUserFile.setFileId(IdUtil.get());
        boxUserFile.setUserId(userId);
        boxUserFile.setParentId(parentId);
        boxUserFile.setFilename(filename);
        boxUserFile.setRealFileId(realFileId);
        boxUserFile.setFolderFlag(folderFlagEnum.getCode());
        boxUserFile.setFileSizeDesc(fileSizeDesc);
        boxUserFile.setFileType(fileType);
        boxUserFile.setDelFlag(DelFlagEnum.NO.getCode());
        boxUserFile.setCreateUser(userId);
        boxUserFile.setCreateTime(DateUtils.getNowDate());
        boxUserFile.setUpdateUser(userId);
        boxUserFile.setUpdateTime(DateUtils.getNowDate());
        handleDuplicateFilename(boxUserFile);
        return boxUserFile;
    }

    /**
     * 处理文件名重复
     */
    private void handleDuplicateFilename(BoxUserFile boxUserFile) {
        String filename = boxUserFile.getFilename(),
                // 没有后缀的文件名
                newFilenameWithoutSuffix,
                // 后缀
                newFilenameSuffix;
        // 定位.的位置
        int newFilenamePointPosition = filename.lastIndexOf(Constants.POINT_STR);
        if (newFilenamePointPosition == Constants.MINUS_ONE_INT) {
            newFilenameWithoutSuffix = filename;
            newFilenameSuffix = StringUtils.EMPTY;
        } else {
            newFilenameWithoutSuffix = filename.substring(Constants.ZERO_INT, newFilenamePointPosition);
            newFilenameSuffix = filename.replace(newFilenameWithoutSuffix, StringUtils.EMPTY);
        }
        int count = getDuplicateFilename(boxUserFile, newFilenameWithoutSuffix);
        if (count == 0) {
            return;
        }

        String newFilename = assembleNewFilename(newFilenameWithoutSuffix, count, newFilenameSuffix);
        boxUserFile.setFilename(newFilename);
    }

    /**
     * 查找文件重复数量
     */
    private int getDuplicateFilename(BoxUserFile boxUserFile, String newFilenameWithoutSuffix) {
        BoxUserFileQuery boxUserFileQuery = new BoxUserFileQuery();
        boxUserFileQuery.setParentId(boxUserFile.getParentId());
        boxUserFileQuery.setFolderFlag(boxUserFile.getFolderFlag());
        boxUserFileQuery.setUserId(boxUserFile.getUserId());
        boxUserFileQuery.setDelFlag(DelFlagEnum.NO.getCode());
        boxUserFileQuery.setFilenameFuzzy(newFilenameWithoutSuffix);
        return boxUserFileMapper.selectCountFileName(boxUserFileQuery);
    }

    /**
     * 拼装新文件名称
     */
    private String assembleNewFilename(String newFilenameWithoutSuffix, int count, String newFilenameSuffix) {
        return newFilenameWithoutSuffix + FileConstants.CN_LEFT_PARENTHESES_STR + count + FileConstants.CN_RIGHT_PARENTHESES_STR + newFilenameSuffix;
    }
}
