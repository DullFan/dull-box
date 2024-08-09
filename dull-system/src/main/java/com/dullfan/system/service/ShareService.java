package com.dullfan.system.service;

import com.dullfan.system.entity.dto.*;
import com.dullfan.system.entity.po.BoxShare;
import com.dullfan.system.entity.po.BoxUserFile;
import com.dullfan.system.entity.po.FilePreview;
import com.dullfan.system.entity.vo.BoxShareUrlListVO;
import com.dullfan.system.entity.vo.BoxUserFileVO;
import com.dullfan.system.entity.vo.ShareDetailVO;
import com.dullfan.system.entity.vo.ShareSimpleDetailVO;

import java.util.List;

public interface ShareService {

    /**
     * 创建分享链接
     */
    BoxShare create(CreateShareUrlDto createShareUrlDto);

    /**
     * 查询用户的分享列表
     */
    List<BoxShareUrlListVO> getShares();

    /**
     * 取消分享
     */
    void cancelShare(List<Long> shareIds);

    /**
     * 校验分享码
     */
    String checkShareCode(CheckShareCodeDto checkShareCodeDto);

    /**
     * 分享详情
     */
    ShareDetailVO detail(QueryShareDetailDto dto);

    /**
     * 查询分享详情简单信息
     */
    ShareSimpleDetailVO simpleDetail(QueryShareSimpleDetailDto dto);

    /**
     * 获取下一级文件列表
     */
    List<BoxUserFile> fileList(QueryChildFileListDto dto);

    /**
     * 保存至我的Box
     */
    void saveFiles(ShareSaveFilesDto dto);

    /**
     * 文件下载
     */
    void download(ShareFileDownloadDto dto);

    /**
     * 刷新受影响状态的分享状态
     */
    void refreshShareStatus(List<Long> allAvailableFileIdList);

    /**
     * 滚动查询数据
     */
    List<Long> rollingQueryShareId(long startId, long limit);

    /**
     * 预览文件
     */
    void preview(ShareFileDownloadDto dto);
}
