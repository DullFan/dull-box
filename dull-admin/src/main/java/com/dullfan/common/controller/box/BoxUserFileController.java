package com.dullfan.common.controller.box;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.dullfan.common.constant.Constants;
import com.dullfan.common.constant.FileConstants;
import com.dullfan.common.controller.ABaseController;
import com.dullfan.common.entity.po.DeleteFile;
import com.dullfan.common.enums.DelFlagEnum;
import com.dullfan.common.storage.StorageEngine;
import com.dullfan.common.utils.SecurityUtils;
import com.dullfan.common.utils.encryption.IdUtil;
import com.dullfan.system.entity.dto.BoxUserFileDto;
import com.dullfan.system.entity.po.BoxUserFile;
import com.dullfan.system.entity.po.CreateFolderPO;
import com.dullfan.system.entity.po.DeleteFilePO;
import com.dullfan.system.entity.po.UpdateFilenamePO;
import com.dullfan.system.entity.query.BoxUserFileQuery;
import com.dullfan.system.service.BoxUserFileService;
import com.dullfan.common.entity.vo.Result;
import com.google.common.base.Splitter;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("BoxUserFileController")
@RequestMapping("/boxUserFile")
@Validated
public class BoxUserFileController extends ABaseController {

    @Resource
    BoxUserFileService boxUserFileService;

    /**
     * 分页查询
     */
    @GetMapping("/loadDataList")
    public Result selectListByPage(BoxUserFileQuery param) {
        return success(boxUserFileService.selectListByPage(param));
    }


    /**
     * 批量新增
     */
    @PostMapping("/insertBatch")
    public Result insertBatch(@RequestBody List<BoxUserFile> listBean) {
        Integer result = boxUserFileService.insertBatch(listBean);
        return determineOperationOutcome(result);
    }

    /**
     * 根据 FileId 查询
     */
    @GetMapping("/selectByFileId")
    public Result selectByFileId(@RequestParam Long fileId) {
        return success(boxUserFileService.selectByFileId(fileId));
    }

    /**
     * 根据 FileId 修改
     */
    @PutMapping("/updateByFileId")
    public Result updateByFileId(@RequestBody BoxUserFile bean, @RequestParam Long fileId) {
        Integer result = boxUserFileService.updateByFileId(bean, fileId);
        return determineOperationOutcome(result);
    }

    /**
     * 根据 FileId 删除
     */
    @DeleteMapping("/deleteByFileId")
    public Result deleteByFileId(@RequestParam Long fileId) {
        Integer result = boxUserFileService.deleteByFileId(fileId);
        return determineOperationOutcome(result);
    }

}