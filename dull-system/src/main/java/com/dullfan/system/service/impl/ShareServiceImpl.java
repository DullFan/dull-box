package com.dullfan.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.dullfan.common.config.BoxServiceConfig;
import com.dullfan.common.constant.CacheConstants;
import com.dullfan.common.constant.Constants;
import com.dullfan.common.constant.FileConstants;
import com.dullfan.common.core.redis.RedisCache;
import com.dullfan.common.entity.po.User;
import com.dullfan.common.enums.DelFlagEnum;
import com.dullfan.common.enums.ShareDayTypeEnum;
import com.dullfan.common.enums.ShareStatusTypeEnum;
import com.dullfan.common.exception.ServiceException;
import com.dullfan.common.utils.DateUtils;
import com.dullfan.common.utils.SecurityUtils;
import com.dullfan.common.utils.encryption.IdUtil;
import com.dullfan.common.utils.uuid.IdUtils;
import com.dullfan.system.bloom.BloomFilter;
import com.dullfan.system.bloom.BloomFilterManager;
import com.dullfan.system.cache.share.ShareCacheService;
import com.dullfan.system.entity.dto.*;
import com.dullfan.system.entity.po.*;
import com.dullfan.system.entity.query.BoxShareFileQuery;
import com.dullfan.system.entity.query.BoxShareQuery;
import com.dullfan.system.entity.vo.*;
import com.dullfan.system.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.URLEncoder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.authentication.DelegatingAuthenticationFailureHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("ShareServiceImpl")
@Slf4j
public class ShareServiceImpl implements ShareService {

    @Resource
    private BoxServiceConfig boxServiceConfig;

    @Resource
    private BoxShareService boxShareService;

    @Resource
    private BoxShareFileService boxShareFileService;

    @Resource
    private BoxUserFileService boxUserFileService;

    @Resource
    private FileService fileService;

    @Resource
    private UserService userService;

    private static final String SHARE_FILE_MISS = "分享的文件丢失";
    private static final String SHARE_CANCELLED = "分享已经被取消";
    private static final String SHARE_EXPIRE = "分享已过期";

    @Resource
    private RedisCache redisCache;

    @Resource
    @Qualifier("shareManualCacheService")
    private ShareCacheService shareCacheService;

    private static final String BLOOM_FILTER_NAME = "SHARE_SIMPLE_DETAIL";

    @Resource
    private BloomFilterManager manager;

    /**
     * 创建分享链接
     */
    @Override
    @Transactional
    public BoxShare create(CreateShareUrlDto createShareUrlDto) {
        saveShare(createShareUrlDto);
        saveShareFiles(createShareUrlDto);
        BoxShare boxShare = assembleShareVO(createShareUrlDto);
        afterCreate(createShareUrlDto,boxShare);
        return boxShare;
    }

    /**
     * 创建分享链接后置处理
     */
    private void afterCreate(CreateShareUrlDto createShareUrlDto, BoxShare boxShare) {
        BloomFilter bloomFilter = manager.getFilter(BLOOM_FILTER_NAME);
        if(Objects.nonNull(bloomFilter)){
            bloomFilter.put(createShareUrlDto.getRecord().getShareId());
            log.info("bloomFilter put success,shareId={}",createShareUrlDto.getRecord().getShareId());
        }
    }

    /**
     * 查询用户分享列表
     */
    @Override
    public List<BoxShareUrlListVO> getShares() {
        return boxShareService.selectShareVOListByUserId(SecurityUtils.getUserId());
    }

    /**
     * 取消分享
     */
    @Override
    @Transactional
    public void cancelShare(List<Long> shareIds) {
        checkUserCancelSharePermission(shareIds);
        doCancelShare(shareIds);
        doCancelShareFiles(shareIds);
    }

    /**
     * 校验分享码
     */
    @Override
    public String checkShareCode(CheckShareCodeDto checkShareCodeDto) {
        checkShareCodeDto.setBoxShare(checkShareStatus(checkShareCodeDto.getShareId()));
        doCheckShareStatus(checkShareCodeDto);
        return generateShareToken(checkShareCodeDto);
    }

    /**
     * 分享详情
     */
    @Override
    public ShareDetailVO detail(QueryShareDetailDto dto) {
        BoxShare boxShare = checkShareStatus(dto.getShareId());
        dto.setBoxShare(boxShare);
        initShareVO(dto);
        assembleShareInfo(dto);
        assembleShareFilesInfo(dto);
        assembleShareUserInfo(dto);
        return dto.getVo();
    }

    /**
     * 查询分享详情简单信息
     */
    @Override
    public ShareSimpleDetailVO simpleDetail(QueryShareSimpleDetailDto dto) {
        log.info("执行查询分享详情简单信息");
        BoxShare boxShare = checkShareStatus(dto.getShareId());
        dto.setRecord(boxShare);
        initShareSimpleVO(dto);
        assembleShareSimpleInfo(dto);
        assembleShareSimpleUserInfo(dto);
        return dto.getVo();
    }

    /**
     * 获取下一级文件列表
     */
    @Override
    public List<BoxUserFile> fileList(QueryChildFileListDto dto) {
        BoxShare record = checkShareStatus(dto.getShareId());
        dto.setRecord(record);
        List<BoxUserFile> allUserFileRecords = checkFileIdIsOnShareStatusAndGetAllShareUserFiles(dto.getShareId(), Lists.newArrayList(dto.getParentId()));
        Map<Long, List<BoxUserFile>> parentIdFileList = allUserFileRecords.stream().collect(Collectors.groupingBy(BoxUserFile::getParentId));
        List<BoxUserFile> boxUserFileVOS = parentIdFileList.get(dto.getParentId());
        if (CollectionUtils.isEmpty(boxUserFileVOS)) {
            return Lists.newArrayList();
        }
        return boxUserFileVOS;
    }

    /**
     * 保存至我的网盘
     */
    @Override
    public void saveFiles(ShareSaveFilesDto dto) {
        BoxShare boxShare = checkShareStatus(dto.getShareId());
        checkFileIdIsONShareStatus(dto.getShareId(), dto.getFileIds());
        CopyFileDto copyFileDto = new CopyFileDto();
        copyFileDto.setFileIds(dto.getFileIds());
        copyFileDto.setTargetParentId(dto.getTargetParentId());
        copyFileDto.setUserId(boxShare.getCreateUser());
        fileService.copy(copyFileDto);
    }

    /**
     * 分享文件下载
     */
    @Override
    public void download(ShareFileDownloadDto dto) {
        checkShareStatus(dto.getShareId());
        checkFileIdIsONShareStatus(dto.getShareId(), Lists.newArrayList(dto.getFileId()));
        doDownload(dto);
    }

    /**
     * 文件预览
     */
    @Override
    public void preview(ShareFileDownloadDto dto) {
        checkShareStatus(dto.getShareId());
        checkFileIdIsONShareStatus(dto.getShareId(), Lists.newArrayList(dto.getFileId()));
        doPreview(dto);
    }

    private void doPreview(ShareFileDownloadDto dto) {
        FilePreview fileDownload = new FilePreview();
        fileDownload.setResponse(dto.getResponse());
        fileDownload.setFileId(dto.getFileId());
        fileDownload.setUserId(dto.getUserId());
        fileService.previewWithoutCheckUser(fileDownload);
    }

    @Override
    public void refreshShareStatus(List<Long> allAvailableFileIdList) {
        List<Long> shareIdList = getShareIdListByFileId(allAvailableFileIdList);
        if (CollectionUtils.isEmpty(shareIdList)) {
            return;
        }
        Set<Long> shareIdSet = Sets.newHashSet(shareIdList);
        shareIdSet.forEach(this::refreshOneShareStatus);
    }

    /**
     * 滚动查询数据
     */
    @Override
    public List<Long> rollingQueryShareId(long startId, long limit) {
        return boxShareService.rollingQueryShareId(startId, limit);
    }

    /**
     * 刷新分享状态
     */
    private void refreshOneShareStatus(Long shareId) {
        BoxShare record = shareCacheService.getById(shareId);
        if (Objects.isNull(record)) {
            return;
        }
        ShareStatusTypeEnum shareStatus = ShareStatusTypeEnum.NORMAL;
        if (!checkShareFileAvailable(shareId)) {
            shareStatus = ShareStatusTypeEnum.FILE_DELETED;
        }
        if (Objects.equals(record.getShareStatus(), shareStatus.getCode())) {
            return;
        }
        doChangeShareStatus(shareId, shareStatus);
    }

    /**
     * 执行刷新文件动作
     */
    private void doChangeShareStatus(Long shareId, ShareStatusTypeEnum shareStatus) {
        BoxShare boxShare = new BoxShare();
        boxShare.setShareStatus(shareStatus.getCode());
        if (!shareCacheService.updateById(shareId,boxShare)) {
            log.error("执行刷新文件动作失败,shareId:" + shareId);
        }
    }

    /**
     * 检查该分享所有文件以及所有的父文件均为正常状态
     */
    private boolean checkShareFileAvailable(Long shareId) {
        List<Long> shareFileIdList = getShareFileIdList(shareId);
        for (Long fileId : shareFileIdList) {
            if (!checkUpFileAvailable(fileId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查该文件以及所有的文件夹信息均为正常状态
     */
    private boolean checkUpFileAvailable(Long fileId) {
        BoxUserFile boxUserFile = boxUserFileService.selectByFileId(fileId);
        if (Objects.isNull(boxUserFile)) {
            return false;
        }
        if (Objects.equals(boxUserFile.getParentId(), FileConstants.TOP_PARENT_ID)) {
            return true;
        }

        return checkUpFileAvailable(boxUserFile.getParentId());
    }

    /**
     * 通过文件ID查询对应的分享ID集合
     */
    private List<Long> getShareIdListByFileId(List<Long> allAvailableFileIdList) {
        List<BoxShareFile> list = boxShareFileService.selectByFileIds(allAvailableFileIdList);
        return list.stream().map(BoxShareFile::getShareId).collect(Collectors.toList());
    }

    /**
     * 执行下载
     */
    private void doDownload(ShareFileDownloadDto dto) {
        FileDownload fileDownload = new FileDownload();
        fileDownload.setResponse(dto.getResponse());
        fileDownload.setFileId(dto.getFileId());
        fileDownload.setUserId(dto.getUserId());
        fileService.downloadWithoutCheckUser(fileDownload);
    }

    /**
     * 校验文件ID是否属于某一个分享
     */
    private void checkFileIdIsONShareStatus(Long shareId, List<Long> fileIds) {
        checkFileIdIsOnShareStatusAndGetAllShareUserFiles(shareId, fileIds);
    }

    /**
     * 校验文件是否处于分享状态,返回该分享的所有文件
     */
    private List<BoxUserFile> checkFileIdIsOnShareStatusAndGetAllShareUserFiles(Long shareId, List<Long> fileIdList) {
        List<Long> shareFileIdList = getShareFileIdList(shareId);
        if (CollectionUtils.isEmpty(shareFileIdList)) {
            return Lists.newArrayList();
        }

        List<BoxUserFile> allFileRecords = boxUserFileService.findAllFileRecordsByFileId(shareFileIdList);
        if (CollectionUtils.isEmpty(allFileRecords)) {
            return Lists.newArrayList();
        }
        allFileRecords = allFileRecords.stream().filter(Objects::nonNull).filter(record -> Objects.equals(record.getDelFlag(), DelFlagEnum.NO.getCode())).collect(Collectors.toList());

        List<Long> allFIleIdList = allFileRecords.stream().map(BoxUserFile::getFileId).toList();
        if (new HashSet<>(allFIleIdList).containsAll(fileIdList)) {
            return allFileRecords;
        }
        throw new ServiceException("分享文件不存在");
    }

    /**
     * 查询分享对应的文件ID集合
     */
    private List<Long> getShareFileIdList(Long shareId) {
        if (Objects.isNull(shareId)) {
            return Lists.newArrayList();
        }
        BoxShareFileQuery boxShareFileQuery = new BoxShareFileQuery();
        boxShareFileQuery.setShareId(shareId);
        List<BoxShareFile> boxShareFiles = boxShareFileService.selectAllList(boxShareFileQuery);
        return boxShareFiles.stream().map(BoxShareFile::getFileId).toList();
    }

    /**
     * 查询分享者信息
     */
    private void assembleShareSimpleUserInfo(QueryShareSimpleDetailDto dto) {
        User user = userService.selectUserByUserId(dto.getRecord().getCreateUser());
        if (Objects.isNull(user)) {
            throw new ServiceException("用户信息查询失败");
        }
        ShareUserInfoVO shareUserInfoVO = new ShareUserInfoVO();
        shareUserInfoVO.setUserId(user.getUserId());
        shareUserInfoVO.setUserName(encryptUserName(user.getUserName()));
        dto.getVo().setShareUserInfoVO(shareUserInfoVO);
    }

    /**
     * 填充分享信息
     */
    private void assembleShareSimpleInfo(QueryShareSimpleDetailDto dto) {
        BoxShare boxShare = dto.getRecord();
        ShareSimpleDetailVO vo = dto.getVo();
        BeanUtils.copyProperties(boxShare, vo);
    }

    /**
     * 初始化实体
     */
    private void initShareSimpleVO(QueryShareSimpleDetailDto dto) {
        dto.setVo(new ShareSimpleDetailVO());
    }

    /**
     * 查询分享者信息
     */
    private void assembleShareUserInfo(QueryShareDetailDto dto) {
        User user = userService.selectUserByUserId(dto.getBoxShare().getCreateUser());
        if (Objects.isNull(user)) {
            throw new ServiceException("用户信息查询失败");
        }
        ShareUserInfoVO shareUserInfoVO = new ShareUserInfoVO();
        shareUserInfoVO.setUserId(user.getUserId());
        shareUserInfoVO.setUserName(encryptUserName(user.getUserName()));
        dto.getVo().setShareUserInfoVO(shareUserInfoVO);
    }

    /**
     * 加密用户名称
     */
    private String encryptUserName(String userName) {
        String substring = userName.substring(Constants.ZERO_INT, Constants.ONE_INT);
        String substringEnd = userName.substring(userName.length() - Constants.ONE_INT);
        return substring + Constants.COMMON_ENCRYPT_STR + substringEnd;
    }

    /**
     * 查询分享对应的文件列表
     */
    private void assembleShareFilesInfo(QueryShareDetailDto dto) {
        List<Long> shareFileIdList = getShareFileIdList(dto.getShareId());
        List<BoxUserFileVO> boxUserFiles = boxUserFileService.selectFileList(null, null, dto.getBoxShare().getCreateUser(), DelFlagEnum.NO.getCode(), shareFileIdList);
        dto.getVo().setBoxUserFileVOList(boxUserFiles);
    }

    /**
     * 查询分享主题信息
     */
    private void assembleShareInfo(QueryShareDetailDto dto) {
        BoxShare boxShare = dto.getBoxShare();
        ShareDetailVO vo = dto.getVo();
        BeanUtils.copyProperties(boxShare, vo);
    }

    /**
     * 初始化文件详情VO实体
     */
    private void initShareVO(QueryShareDetailDto dto) {
        dto.setVo(new ShareDetailVO());
    }

    /**
     * 生成短期的token
     */
    private String generateShareToken(CheckShareCodeDto checkShareCodeDto) {
        BoxShare record = checkShareCodeDto.getBoxShare();
        // 10位随机数 + UUID
        String key = RandomStringUtils.randomAlphanumeric(10) + IdUtils.simpleUUID();
        redisCache.setCacheObject(CacheConstants.SHARE_CODE_KEY + key, record, Constants.SHARE_CODE_EXPIRATION, TimeUnit.DAYS);
        return key;
    }

    /**
     * 执行校验分享码
     */
    private void doCheckShareStatus(CheckShareCodeDto checkShareCodeDto) {
        String shareCode = checkShareCodeDto.getBoxShare().getShareCode();
        if (!Objects.equals(checkShareCodeDto.getShareCode(), shareCode)) {
            throw new ServiceException("分享码错误");
        }
    }

    /**
     * 检查分享的状态是否正常
     */
    private BoxShare checkShareStatus(Long shareId) {
        BoxShare record = shareCacheService.getById(shareId);
        if (Objects.isNull(record)) {
            throw new ServiceException(SHARE_CANCELLED);
        }
        if (Objects.equals(ShareStatusTypeEnum.FILE_DELETED.getCode(), record.getShareStatus())) {
            throw new ServiceException(SHARE_FILE_MISS);
        }
        if (Objects.equals(ShareDayTypeEnum.PERMANENT_VALIDITY.getCode(), record.getShareDayType())) {
            return record;
        }
        if (record.getShareEndTime().before(DateUtils.getNowDate())) {
            throw new ServiceException(SHARE_EXPIRE);
        }
        return record;
    }

    /**
     * 删除关联数据
     */
    private void doCancelShareFiles(List<Long> shareIds) {
        if (boxShareFileService.deleteByShareIds(shareIds) == 0) {
            throw new ServiceException("取消分享失败");
        }
    }

    /**
     * 取消文件分享
     */
    private void doCancelShare(List<Long> shareIds) {
        if (!shareCacheService.removeByIds(shareIds)) {
            throw new ServiceException("取消分享失败");
        }
    }

    /**
     * 检查用户是否拥有权限
     */
    private void checkUserCancelSharePermission(List<Long> shareIds) {
        List<BoxShare> boxShares = shareCacheService.getByIds(shareIds);
        if (CollectionUtils.isEmpty(boxShares)) {
            throw new ServiceException("无权限操作");
        }
        if (shareIds.size() != boxShares.size()) {
            throw new ServiceException("无权限操作");
        }
    }

    /**
     * 拼装返回
     */
    private BoxShare assembleShareVO(CreateShareUrlDto createShareUrlDto) {
        return createShareUrlDto.getRecord();
    }

    /**
     * 保存分享和分享文件的关联关系
     */
    private void saveShareFiles(CreateShareUrlDto createShareUrlDto) {
        List<BoxShareFile> list = createShareUrlDto.getShareFIleIds().stream().map(item -> {
            BoxShareFile boxShareFile = new BoxShareFile();
            boxShareFile.setShareId(createShareUrlDto.getRecord().getShareId());
            boxShareFile.setCreateUser(createShareUrlDto.getUserId());
            boxShareFile.setCreateTime(new Date());
            boxShareFile.setFileId(item);
            return boxShareFile;
        }).toList();
        if (boxShareFileService.insertBatch(list) == 0) {
            throw new ServiceException("分享和分享文件关联失败");
        }
    }

    /**
     * 保存分享信息
     */
    private void saveShare(CreateShareUrlDto createShareUrlDto) {
        BoxShare boxShare = new BoxShare();
        boxShare.setShareId(IdUtil.get());
        boxShare.setShareName(createShareUrlDto.getShareName());
        boxShare.setShareType(createShareUrlDto.getShareType());
        boxShare.setShareDayType(createShareUrlDto.getShareDayType());
        Integer shareDayByCode = ShareDayTypeEnum.getShareDayByCode(createShareUrlDto.getShareDayType());
        if (Objects.equals(shareDayByCode, Constants.MINUS_ONE_INT)) {
            throw new ServiceException("分享天数非法");
        }
        boxShare.setShareDay(shareDayByCode);
        boxShare.setShareEndTime(DateUtil.offsetDay(new Date(), shareDayByCode));
        boxShare.setShareUrl(createShareUrl(boxShare.getShareId()));
        boxShare.setShareCode(createShareCode());
        boxShare.setShareStatus(ShareStatusTypeEnum.NORMAL.getCode());
        boxShare.setCreateUser(createShareUrlDto.getUserId());
        boxShare.setCreateTime(new Date());
        if (boxShareService.insert(boxShare) == 0) {
            throw new ServiceException("保存分享信息失败");
        }
        createShareUrlDto.setRecord(boxShare);
    }

    /**
     * 创建分享的分享码
     */
    private String createShareCode() {
        return RandomStringUtils.randomAlphanumeric(4).toLowerCase();
    }

    /**
     * 创建分享的URL
     */
    private String createShareUrl(Long shareId) {
        if (Objects.isNull(shareId)) {
            throw new ServiceException("分享的ID不能为空");
        }
        String sharePrefix = boxServiceConfig.getSharePrefix();
        // 验证是否有斜杆,没有的话添加一个斜杆
        if (sharePrefix.lastIndexOf(Constants.SLASH_STR) == Constants.MINUS_ONE_INT) {
            sharePrefix += Constants.SLASH_STR;
        }
        String shareToken = sharePrefix + IdUtils.simpleUUID();
        redisCache.setCacheObject(shareToken,shareId);
        return shareToken;
    }
}
