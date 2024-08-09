package com.dullfan.common.controller.box;

import java.util.List;

import com.dullfan.common.controller.ABaseController;
import com.dullfan.system.entity.po.BoxFile;
import com.dullfan.system.entity.query.BoxFileQuery;
import com.dullfan.system.service.BoxFileService;
import com.dullfan.common.entity.vo.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController("BoxFileController")
@RequestMapping("/boxFile")
public class BoxFileController extends ABaseController {

    @Resource
    BoxFileService boxFileService;

    /**
     * 分页查询
     */
    @GetMapping("/loadDataList")
    public Result selectListByPage(BoxFileQuery param){
        return success(boxFileService.selectListByPage(param));
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody BoxFile bean){
        Integer result = boxFileService.insert(bean);
        return determineOperationOutcome(result);
    }

    /**
     * 批量新增
     */
    @PostMapping("/insertBatch")
    public Result insertBatch(@RequestBody List<BoxFile> listBean){
        Integer result = boxFileService.insertBatch(listBean);
        return determineOperationOutcome(result);
    }

    /**
     * 根据 FileId 查询
     */
    @GetMapping("/selectByFileId")
    public Result selectByFileId(@RequestParam Long fileId){
        return success(boxFileService.selectByFileId(fileId));
    }

    /**
     * 根据 FileId 修改
     */
    @PutMapping("/updateByFileId")
    public Result updateByFileId(@RequestBody BoxFile bean,@RequestParam Long fileId){
        Integer result = boxFileService.updateByFileId(bean,fileId);
        return determineOperationOutcome(result);
    }


    /**
     * 根据 FileId 删除
     */
    @DeleteMapping("/deleteByFileId")
    public Result deleteByFileId(@RequestParam Long fileId){
        Integer result = boxFileService.deleteByFileId(fileId);
        return determineOperationOutcome(result);
    }

}