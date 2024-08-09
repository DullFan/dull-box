package com.dullfan.common.controller.box;

import com.dullfan.common.controller.ABaseController;
import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.common.entity.vo.Result;
import com.dullfan.common.utils.SecurityUtils;
import com.dullfan.system.entity.po.BoxUserFile;
import com.dullfan.system.entity.po.DeleteFilePO;
import com.dullfan.system.entity.po.RestorePO;
import com.dullfan.system.service.BoxUserFileService;
import com.dullfan.system.service.RecycleService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RestController("RecycleController")
@RequestMapping("/recycle")
public class RecycleController extends ABaseController {

    @Resource
    private RecycleService recycleService;

    @Resource
    private BoxUserFileService boxUserFileService;

    /**
     * 获取回收站文件列表
     */
    @GetMapping
    public Result recycles(){
        List<BoxUserFile> boxUserFileVOList = recycleService.recycles();
        return success(boxUserFileVOList);
    }

    /**
     * 根据 FileIds 逻辑删除
     */
    @DeleteMapping
    public Result softDeleteByFileId(@Validated @RequestBody DeleteFilePO deleteFilePO) {
        if(!SecurityUtils.isAdmin(getUserId())){
            deleteFilePO.setUserId(getUserId());
        }
        Integer result = boxUserFileService.softDeleteByFileId(deleteFilePO);
        return determineOperationOutcome(result);
    }

    /**
     * 文件还原
     */
    @PutMapping("/restore")
    public Result restore(@Validated @RequestBody RestorePO restorePO){
        if(!SecurityUtils.isAdmin(getUserId())){
            restorePO.setUserId(getUserId());
        }
        boxUserFileService.restoreFileByFileId(restorePO);
        return success();
    }

    /**
     * 彻底删除文件
     */
    @DeleteMapping("/delFile")
    public Result deleteFile(@Validated @RequestBody DeleteFilePO deleteFilePO){
        if(!SecurityUtils.isAdmin(getUserId())){
            deleteFilePO.setUserId(getUserId());
        }
        recycleService.delete(deleteFilePO);
        return success();
    }


}
