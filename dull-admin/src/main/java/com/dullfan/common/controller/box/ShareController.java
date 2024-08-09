package com.dullfan.common.controller.box;


import com.dullfan.common.annotation.NeedShareCode;
import com.dullfan.common.constant.Constants;
import com.dullfan.common.controller.ABaseController;
import com.dullfan.common.core.redis.RedisCache;
import com.dullfan.common.entity.vo.Result;
import com.dullfan.common.utils.DateUtils;
import com.dullfan.common.utils.SecurityUtils;
import com.dullfan.common.utils.ShareIdUtils;
import com.dullfan.common.utils.encryption.IdUtil;
import com.dullfan.system.entity.dto.*;
import com.dullfan.system.entity.po.*;
import com.dullfan.system.entity.vo.BoxShareUrlListVO;
import com.dullfan.system.entity.vo.BoxUserFileVO;
import com.dullfan.system.entity.vo.ShareDetailVO;
import com.dullfan.system.entity.vo.ShareSimpleDetailVO;
import com.dullfan.system.service.BoxShareService;
import com.dullfan.system.service.ShareService;
import com.google.common.base.Splitter;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("ShareController")
@RequestMapping("/share")
@Validated
public class ShareController extends ABaseController {

    @Resource
    private ShareService shareService;

    @Resource
    RedisCache redisCache;

    /**
     * 创建分享链接
     */
    @PostMapping
    public Result creat(@Validated @RequestBody CreateShareUrlPO createShareUrlPO) {
        CreateShareUrlDto createShareUrlDto = new CreateShareUrlDto();
        // 不是管理员的话直接将userId设置为当前登录用户
        if (!SecurityUtils.isAdmin(getUserId())) {
            createShareUrlDto.setUserId(getUserId());
        }
        BeanUtils.copyProperties(createShareUrlPO, createShareUrlDto);
        List<Long> fileIds = Splitter.on(Constants.COMMON_SEPARATOR)
                .splitToList(createShareUrlPO.getShareFileIds())
                .stream().map(IdUtil::decrypt)
                .toList();
        createShareUrlDto.setShareFIleIds(fileIds);
        return success(shareService.create(createShareUrlDto));
    }

    /**
     * 查询用户分享列表
     */
    @GetMapping
    public Result list() {
        List<BoxShareUrlListVO> lists = shareService.getShares();
        return success(lists);
    }

    /**
     * 取消分享
     */
    @DeleteMapping
    public Result cancelShare(@Validated @RequestBody CancelSharePO cancelSharePO) {
        List<Long> shareIds =
                Splitter.on(Constants.COMMON_SEPARATOR)
                        .splitToList(cancelSharePO.getShareIds())
                        .stream()
                        .map(item-> {
                            Long shareId = redisCache.getCacheObject(item);
                            redisCache.deleteObject(item);
                            return shareId;
                        })
                        .toList();
        System.out.println(shareIds);
        System.out.println("--->");
        shareService.cancelShare(shareIds);
        return success();
    }

    /**
     * 校验分享码
     */
    @PostMapping("/code/check")
    public Result checkShareCode(@Validated @RequestBody CheckShareCodePO checkShareCodePO) {
        CheckShareCodeDto checkShareCodeDto = new CheckShareCodeDto();
        checkShareCodeDto.setShareCode(checkShareCodePO.getShareCode());
        checkShareCodeDto.setShareId(redisCache.getCacheObject(checkShareCodePO.getShareId()));
        String token = shareService.checkShareCode(checkShareCodeDto);
        return success(token);
    }

    /**
     * 分享详情
     */
    @GetMapping("/detail")
    @NeedShareCode
    public Result detail() {
        QueryShareDetailDto dto = new QueryShareDetailDto();
        dto.setShareId(ShareIdUtils.get());
        ShareDetailVO vo = shareService.detail(dto);
        return success(vo);
    }

    /**
     * 查询简单详情
     */
    @GetMapping("/simple")
    public Result simple(@RequestParam(value = "shareId") @NotBlank(message = "分享 id 不能为空") String shareId) {
        QueryShareSimpleDetailDto dto = new QueryShareSimpleDetailDto();
        dto.setShareId(IdUtil.decrypt(shareId));
        ShareSimpleDetailVO vo = shareService.simpleDetail(dto);
        return success(vo);
    }

    /**
     * 获取下一级文件列表
     */
    @NeedShareCode
    @GetMapping("/file/list")
    public Result fileList(@RequestParam(value = "parentId") @NotBlank(message = "文件父ID不能为空") String parentId) {
        QueryChildFileListDto dto = new QueryChildFileListDto();
        dto.setParentId(IdUtil.decrypt(parentId));
        dto.setShareId(ShareIdUtils.get());
        List<BoxUserFile> result = shareService.fileList(dto);
        return success(result);
    }


    /**
     * 保存至我的Box
     */
    @NeedShareCode
    @PostMapping("/save")
    public Result saveFiles(@Validated @RequestBody ShareSaveFilesPO shareSaveFilesPO) {
        ShareSaveFilesDto dto = new ShareSaveFilesDto();
        List<Long> fileIds = Splitter
                .on(Constants.COMMON_SEPARATOR)
                .splitToList(shareSaveFilesPO.getFileIds())
                .stream()
                .map(IdUtil::decrypt)
                .toList();
        dto.setFileIds(fileIds);
        dto.setShareId(ShareIdUtils.get());
        dto.setTargetParentId(IdUtil.decrypt(shareSaveFilesPO.getTargetFileId()));
        dto.setUserId(SecurityUtils.getUserId());
        shareService.saveFiles(dto);
        return success();
    }

    /**
     * 文件下载
     */
    @GetMapping("/download")
    @NeedShareCode
    public void download(@NotBlank(message = "文件ID不能为空") @RequestParam("fileId") String fileId, HttpServletResponse response) {
        ShareFileDownloadDto dto = new ShareFileDownloadDto();
        dto.setFileId(IdUtil.decrypt(fileId));
        dto.setUserId(SecurityUtils.getUserId());
        dto.setShareId(ShareIdUtils.get());
        dto.setResponse(response);
        shareService.download(dto);
    }

    /**
     * 文件预览
     */
    @GetMapping("/preview")
    public void preview(
            HttpServletResponse response,
            @Validated @NotBlank(message = "文件ID不能为空") @RequestParam(value = "fileId", required = false) String fileId) {
        ShareFileDownloadDto dto = new ShareFileDownloadDto();
        dto.setFileId(IdUtil.decrypt(fileId));
        dto.setUserId(SecurityUtils.getUserId());
        dto.setShareId(ShareIdUtils.get());
        dto.setResponse(response);
        shareService.preview(dto);
    }

}
