package com.dullfan.common.controller.box;

import java.util.List;

import com.dullfan.common.controller.ABaseController;
import com.dullfan.system.entity.po.BoxShareFile;
import com.dullfan.system.entity.query.BoxShareFileQuery;
import com.dullfan.system.service.BoxShareFileService;
import com.dullfan.common.entity.vo.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController("BoxShareFileController")
@RequestMapping("/boxShareFile")
public class BoxShareFileController extends ABaseController {

    @Resource
    BoxShareFileService boxShareFileService;

    /**
     * 分页查询
     */
    @GetMapping("/loadDataList")
    public Result selectListByPage(BoxShareFileQuery param){
        return success(boxShareFileService.selectListByPage(param));
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody BoxShareFile bean){
        Integer result = boxShareFileService.insert(bean);
        return determineOperationOutcome(result);
    }

    /**
     * 批量新增
     */
    @PostMapping("/insertBatch")
    public Result insertBatch(@RequestBody List<BoxShareFile> listBean){
        Integer result = boxShareFileService.insertBatch(listBean);
        return determineOperationOutcome(result);
    }

    /**
     * 根据 Id 查询
     */
    @GetMapping("/selectById")
    public Result selectById(@RequestParam Long id){
        return success(boxShareFileService.selectById(id));
    }

    /**
     * 根据 Id 修改
     */
    @PutMapping("/updateById")
    public Result updateById(@RequestBody BoxShareFile bean,@RequestParam Long id){
        Integer result = boxShareFileService.updateById(bean,id);
        return determineOperationOutcome(result);
    }


    /**
     * 根据 Id 删除
     */
    @DeleteMapping("/deleteById")
    public Result deleteById(@RequestParam Long id){
        Integer result = boxShareFileService.deleteById(id);
        return determineOperationOutcome(result);
    }

    /**
     * 根据 Id 批量删除
     */
    @DeleteMapping("/deleteByIdBatch")
    public Result deleteByIdBatch(@RequestParam List<Integer> list){
        Integer result = boxShareFileService.deleteByIdBatch(list);
        return determineOperationOutcome(result);
    }
    /**
     * 根据 ShareIdAndFileId 查询
     */
    @GetMapping("/selectByShareIdAndFileId")
    public Result selectByShareIdAndFileId(@RequestParam Long shareId,@RequestParam Long fileId){
        return success(boxShareFileService.selectByShareIdAndFileId(shareId,fileId));
    }

    /**
     * 根据 ShareIdAndFileId 修改
     */
    @PutMapping("/updateByShareIdAndFileId")
    public Result updateByShareIdAndFileId(@RequestBody BoxShareFile bean,@RequestParam Long shareId,@RequestParam Long fileId){
        Integer result = boxShareFileService.updateByShareIdAndFileId(bean,shareId,fileId);
        return determineOperationOutcome(result);
    }


    /**
     * 根据 ShareIdAndFileId 删除
     */
    @DeleteMapping("/deleteByShareIdAndFileId")
    public Result deleteByShareIdAndFileId(@RequestParam Long shareId,@RequestParam Long fileId){
        Integer result = boxShareFileService.deleteByShareIdAndFileId(shareId,fileId);
        return determineOperationOutcome(result);
    }

}