package com.dullfan.common.controller.box;

import java.util.Date;
import java.util.List;

import com.dullfan.common.controller.ABaseController;
import com.dullfan.common.entity.vo.Result;
import com.dullfan.system.entity.po.BoxUserSearchHistory;
import com.dullfan.system.entity.query.BoxUserSearchHistoryQuery;
import com.dullfan.system.service.BoxUserSearchHistoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController("BoxUserSearchHistoryController")
@RequestMapping("/boxUserSearchHistory")
public class BoxUserSearchHistoryController extends ABaseController {

    @Resource
    BoxUserSearchHistoryService boxUserSearchHistoryService;

    /**
     * 分页查询
     */
    @GetMapping("/loadDataList")
    public Result selectListByPage(BoxUserSearchHistoryQuery param){
        return success(boxUserSearchHistoryService.selectListByPage(param));
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody BoxUserSearchHistory bean){
        Integer result = boxUserSearchHistoryService.insert(bean);
        return determineOperationOutcome(result);
    }

    /**
     * 批量新增
     */
    @PostMapping("/insertBatch")
    public Result insertBatch(@RequestBody List<BoxUserSearchHistory> listBean){
        Integer result = boxUserSearchHistoryService.insertBatch(listBean);
        return determineOperationOutcome(result);
    }

    /**
     * 根据 Id 查询
     */
    @GetMapping("/selectById")
    public Result selectById(@RequestParam Long id){
        return success(boxUserSearchHistoryService.selectById(id));
    }

    /**
     * 根据 Id 修改
     */
    @PutMapping("/updateById")
    public Result updateById(@RequestBody BoxUserSearchHistory bean,@RequestParam Long id){
        Integer result = boxUserSearchHistoryService.updateById(bean,id);
        return determineOperationOutcome(result);
    }


    /**
     * 根据 Id 删除
     */
    @DeleteMapping("/deleteById")
    public Result deleteById(@RequestParam Long id){
        Integer result = boxUserSearchHistoryService.deleteById(id);
        return determineOperationOutcome(result);
    }

    /**
     * 根据 Id 批量删除
     */
    @DeleteMapping("/deleteByIdBatch")
    public Result deleteByIdBatch(@RequestParam List<Integer> list){
        Integer result = boxUserSearchHistoryService.deleteByIdBatch(list);
        return determineOperationOutcome(result);
    }
    /**
     * 根据 UserIdAndSearchContentAndUpdateTime 查询
     */
    @GetMapping("/selectByUserIdAndSearchContentAndUpdateTime")
    public Result selectByUserIdAndSearchContentAndUpdateTime(@RequestParam Long userId,@RequestParam String searchContent,@RequestParam Date updateTime){
        return success(boxUserSearchHistoryService.selectByUserIdAndSearchContentAndUpdateTime(userId,searchContent,updateTime));
    }

    /**
     * 根据 UserIdAndSearchContentAndUpdateTime 修改
     */
    @PutMapping("/updateByUserIdAndSearchContentAndUpdateTime")
    public Result updateByUserIdAndSearchContentAndUpdateTime(@RequestBody BoxUserSearchHistory bean,@RequestParam Long userId,@RequestParam String searchContent,@RequestParam Date updateTime){
        Integer result = boxUserSearchHistoryService.updateByUserIdAndSearchContentAndUpdateTime(bean,userId,searchContent,updateTime);
        return determineOperationOutcome(result);
    }


    /**
     * 根据 UserIdAndSearchContentAndUpdateTime 删除
     */
    @DeleteMapping("/deleteByUserIdAndSearchContentAndUpdateTime")
    public Result deleteByUserIdAndSearchContentAndUpdateTime(@RequestParam Long userId,@RequestParam String searchContent,@RequestParam Date updateTime){
        Integer result = boxUserSearchHistoryService.deleteByUserIdAndSearchContentAndUpdateTime(userId,searchContent,updateTime);
        return determineOperationOutcome(result);
    }

    /**
     * 根据 UserIdAndSearchContent 查询
     */
    @GetMapping("/selectByUserIdAndSearchContent")
    public Result selectByUserIdAndSearchContent(@RequestParam Long userId,@RequestParam String searchContent){
        return success(boxUserSearchHistoryService.selectByUserIdAndSearchContent(userId,searchContent));
    }

    /**
     * 根据 UserIdAndSearchContent 修改
     */
    @PutMapping("/updateByUserIdAndSearchContent")
    public Result updateByUserIdAndSearchContent(@RequestBody BoxUserSearchHistory bean,@RequestParam Long userId,@RequestParam String searchContent){
        Integer result = boxUserSearchHistoryService.updateByUserIdAndSearchContent(bean,userId,searchContent);
        return determineOperationOutcome(result);
    }


    /**
     * 根据 UserIdAndSearchContent 删除
     */
    @DeleteMapping("/deleteByUserIdAndSearchContent")
    public Result deleteByUserIdAndSearchContent(@RequestParam Long userId,@RequestParam String searchContent){
        Integer result = boxUserSearchHistoryService.deleteByUserIdAndSearchContent(userId,searchContent);
        return determineOperationOutcome(result);
    }

}