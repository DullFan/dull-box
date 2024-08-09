package com.dullfan.common.controller.box;

import com.dullfan.common.constant.Constants;
import com.dullfan.common.constant.FileConstants;
import com.dullfan.common.controller.ABaseController;
import com.dullfan.common.entity.vo.Result;
import com.dullfan.common.enums.DelFlagEnum;
import com.dullfan.common.utils.SecurityUtils;
import com.dullfan.common.utils.StringUtils;
import com.dullfan.common.utils.encryption.IdUtil;
import com.dullfan.system.entity.dto.BoxUserFileDto;
import com.dullfan.system.entity.dto.CopyFileDto;
import com.dullfan.system.entity.dto.FileSearchDto;
import com.dullfan.system.entity.dto.TransferFileDto;
import com.dullfan.system.entity.po.*;
import com.dullfan.system.entity.vo.*;
import com.dullfan.system.service.FileService;
import com.google.common.base.Splitter;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController("FileController")
@RequestMapping("/file")
public class FileController extends ABaseController {

    @Resource
    private FileService fileService;

    /**
     * 查询列表
     */
    @GetMapping("/list")
    public Result selectList(@NotBlank(message = "父文件ID不能为空") @RequestParam(value = "parentId", required = false) String parentId,
                             @RequestParam(value = "fileTypes", required = false, defaultValue = FileConstants.ALL_FILE_TYPE) String fileTypes
    ) {
        Long realParentId = null;
        if(!parentId.equals("-1")){
            realParentId = IdUtil.decrypt(parentId);
        }
        List<Integer> fileTypeList = null;
        if (!Objects.equals(FileConstants.ALL_FILE_TYPE, fileTypes)) {
            fileTypeList = Splitter.on(Constants.COMMON_SEPARATOR).splitToList(fileTypes)
                    .stream().map(Integer::valueOf)
                    .toList();
        }
        BoxUserFileDto boxUserFileDto = new BoxUserFileDto();
        boxUserFileDto.setParentId(realParentId);
        boxUserFileDto.setFileTypeArray(fileTypeList);
        boxUserFileDto.setUserId(getUserId());
        boxUserFileDto.setDelFlag(DelFlagEnum.NO.getCode());

        List<BoxUserFile> list = fileService.selectList(boxUserFileDto);
        return success(list);
    }

    /**
     * 创建文件夹
     */
    @PostMapping("/createFolder")
    public Result createFolder(@Validated @RequestBody CreateFolderPO createFolderPO) {
        if (!SecurityUtils.isAdmin(getUserId())) {
            createFolderPO.setUserId(getUserId());
        }
        Long fileId = fileService.createFolder(createFolderPO);
        return success(IdUtil.encrypt(fileId));
    }

    /**
     * 文件重命名
     */
    @PutMapping("/updateFilename")
    public Result updateFilename(@Validated @RequestBody UpdateFilenamePO updateFilenamePO) {
        if (!SecurityUtils.isAdmin(getUserId())) {
            updateFilenamePO.setUserId(getUserId());
        }
        return determineOperationOutcome(fileService.updateFilename(updateFilenamePO));
    }

    /**
     * 文件秒传
     */
    @PostMapping("/secUpload")
    public Result secUpload(@Validated @RequestBody SecUploadFilePO secUploadFilePO) {
        boolean flag = fileService.secUpload(secUploadFilePO);
        if (flag) {
            return success();
        }
        return error("文件唯一标识不存在");
    }

    /**
     * 单文件上传
     */
    @PostMapping("/upload")
    public Result secUpload(@Validated FileUploadPO fileUploadPO) throws IOException {
        fileService.upload(fileUploadPO);
        return success();
    }

    /**
     * 文件分片上传
     */
    @PostMapping("/chunkUpload")
    public Result chunkUpload(@Validated FileChunkUploadPO fileUploadPO) throws IOException {
        FileChunkUploadVO fileChunkUploadVO = fileService.chunkUpload(fileUploadPO);
        return success(fileChunkUploadVO);
    }

    /**
     * 查询已经上传的文件分片列表
     */
    @GetMapping("/chunkUpload")
    public Result getUploadedChunks(@Validated QueryUploadedChunksPO queryUploadedChunksPO) throws IOException {
        UploadedChunksVO uploadedChunks = fileService.getUploadedChunks(queryUploadedChunksPO);
        return success(uploadedChunks);
    }

    /**
     * 文件分片合并
     */
    @PostMapping("/mergeFile")
    public Result mergeFile(@Validated @RequestBody FileChunkMergePO fileChunkMergePO) throws IOException {
        fileService.mergeFile(fileChunkMergePO);
        return success();
    }

    /**
     * 文件下载
     */
    @GetMapping("/download")
    public void downloadFile(
            HttpServletResponse response,
            @Validated @NotBlank(message = "文件ID不能为空") @RequestParam(value = "fileId", required = false) String fileId) {
        FileDownload fileDownload = new FileDownload();
        fileDownload.setFileId(IdUtil.decrypt(fileId));
        fileDownload.setUserId(SecurityUtils.getUserId());
        fileDownload.setResponse(response);
        fileService.download(fileDownload);
    }

    /**
     * 文件预览
     */
    @GetMapping("/preview")
    public void preview(
            HttpServletResponse response,
            @Validated @NotBlank(message = "文件ID不能为空") @RequestParam(value = "fileId", required = false) String fileId) {
        FilePreview filePreview = new FilePreview();
        filePreview.setFileId(IdUtil.decrypt(fileId));
        filePreview.setUserId(SecurityUtils.getUserId());
        filePreview.setResponse(response);
        fileService.preview(filePreview);
    }

    /**
     * 查询文件夹树
     */
    @GetMapping("/folder/tree")
    public Result getFolderTree(){
        List<FolderTreeNodeVO> folderTree = fileService.getFolderTree();
        return success(folderTree);
    }

    /**
     * 文件转移
     */
    @PostMapping("/transfer")
    public Result transfer(@Validated @RequestBody TransferFilePO transferFilePO){
        TransferFileDto transferFileDto = new TransferFileDto();
        List<Long> fileIdList = Splitter.on(Constants.COMMON_SEPARATOR)
                .splitToList(transferFilePO.getFileIds())
                .stream().map(IdUtil::decrypt).toList();
        transferFileDto.setFileIds(fileIdList);
        transferFileDto.setTargetParentId(IdUtil.decrypt(transferFilePO.getTargetParentId()));
        fileService.transfer(transferFileDto);
        return success();
    }

    /**
     * 文件复制
     */
    @PostMapping("/copy")
    public Result copy(@Validated @RequestBody CopyFilePO copyFilePO){
        CopyFileDto copyFileDto = new CopyFileDto();
        List<Long> fileIdList = Splitter.on(Constants.COMMON_SEPARATOR)
                .splitToList(copyFilePO.getFileIds())
                .stream().map(IdUtil::decrypt).toList();
        copyFileDto.setFileIds(fileIdList);
        copyFileDto.setTargetParentId(IdUtil.decrypt(copyFilePO.getTargetParentId()));
        if (!SecurityUtils.isAdmin(getUserId())) {
            copyFileDto.setUserId(getUserId());
        }
        fileService.copy(copyFileDto);
        return success();
    }

    /**
     * 文件搜索
     */
    @GetMapping("/search")
    public Result search(@Validated FileSearchPO fileSearchPO){
        FileSearchDto fileSearchDto = new FileSearchDto();
        fileSearchDto.setKeyword(fileSearchPO.getKeyword());
        fileSearchDto.setUserId(SecurityUtils.getUserId());
        String fileTypes = fileSearchPO.getFileTypes();
        if(StringUtils.isNotBlank(fileTypes) && !Objects.equals(FileConstants.ALL_FILE_TYPE,fileTypes)){
            List<Integer> list = Splitter.on(Constants.COMMON_SEPARATOR).splitToList(fileTypes).stream().map(Integer::valueOf).toList();
            fileSearchDto.setFileTypes(list);
        }
        List<BoxUserFileVO> resultVOS = fileService.search(fileSearchDto);
        return success(resultVOS);
    }

    /**
     * 查询文件面包屑
     */
    @GetMapping("/breadcrumbs")
    public Result getBreadcrumbs(@Validated @NotBlank(message = "文件ID不能为空") String fileId){
        return success(fileService.getBreadcrumbs(IdUtil.decrypt(fileId)));
    }

}
