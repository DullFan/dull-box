package com.dullfan.common.controller.box;

import java.util.List;

import com.dullfan.common.controller.ABaseController;
import com.dullfan.common.entity.vo.Result;
import com.dullfan.system.entity.po.BoxErrorLog;
import com.dullfan.system.entity.query.BoxErrorLogQuery;
import com.dullfan.system.service.BoxErrorLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController("BoxErrorLogController")
@RequestMapping("/boxErrorLog")
public class BoxErrorLogController extends ABaseController {

    @Resource
    BoxErrorLogService boxErrorLogService;

    /**
     * 分页查询
     */
    @GetMapping("/loadDataList")
    public Result selectListByPage(BoxErrorLogQuery param){
        return success(boxErrorLogService.selectListByPage(param));
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody BoxErrorLog bean){
        Integer result = boxErrorLogService.insert(bean);
        return determineOperationOutcome(result);
    }

    /**
     * 批量新增
     */
    @PostMapping("/insertBatch")
    public Result insertBatch(@RequestBody List<BoxErrorLog> listBean){
        Integer result = boxErrorLogService.insertBatch(listBean);
        return determineOperationOutcome(result);
    }

    /**
     * 根据 Id 查询
     */
    @GetMapping("/selectById")
    public Result selectById(@RequestParam Long id){
        return success(boxErrorLogService.selectById(id));
    }

    /**
     * 根据 Id 修改
     */
    @PutMapping("/updateById")
    public Result updateById(@RequestBody BoxErrorLog bean,@RequestParam Long id){
        Integer result = boxErrorLogService.updateById(bean,id);
        return determineOperationOutcome(result);
    }


    /**
     * 根据 Id 删除
     */
    @DeleteMapping("/deleteById")
    public Result deleteById(@RequestParam Long id){
        Integer result = boxErrorLogService.deleteById(id);
        return determineOperationOutcome(result);
    }

    /**
     * 根据 Id 批量删除
     */
    @DeleteMapping("/deleteByIdBatch")
    public Result deleteByIdBatch(@RequestParam List<Integer> list){
        Integer result = boxErrorLogService.deleteByIdBatch(list);
        return determineOperationOutcome(result);
    }
}