package com.dullfan.common.controller.box;

import java.util.List;

import com.dullfan.common.controller.ABaseController;
import com.dullfan.system.entity.po.BoxFileChunk;
import com.dullfan.system.entity.query.BoxFileChunkQuery;
import com.dullfan.system.service.BoxFileChunkService;
import com.dullfan.common.entity.vo.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController("BoxFileChunkController")
@RequestMapping("/boxFileChunk")
public class BoxFileChunkController extends ABaseController {

    @Resource
    BoxFileChunkService boxFileChunkService;

    /**
     * 分页查询
     */
    @GetMapping("/loadDataList")
    public Result selectListByPage(BoxFileChunkQuery param){
        return success(boxFileChunkService.selectListByPage(param));
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody BoxFileChunk bean){
        Integer result = boxFileChunkService.insert(bean);
        return determineOperationOutcome(result);
    }

    /**
     * 批量新增
     */
    @PostMapping("/insertBatch")
    public Result insertBatch(@RequestBody List<BoxFileChunk> listBean){
        Integer result = boxFileChunkService.insertBatch(listBean);
        return determineOperationOutcome(result);
    }

    /**
     * 根据 Id 查询
     */
    @GetMapping("/selectById")
    public Result selectById(@RequestParam Long id){
        return success(boxFileChunkService.selectById(id));
    }

    /**
     * 根据 Id 修改
     */
    @PutMapping("/updateById")
    public Result updateById(@RequestBody BoxFileChunk bean,@RequestParam Long id){
        Integer result = boxFileChunkService.updateById(bean,id);
        return determineOperationOutcome(result);
    }


    /**
     * 根据 Id 删除
     */
    @DeleteMapping("/deleteById")
    public Result deleteById(@RequestParam Long id){
        Integer result = boxFileChunkService.deleteById(id);
        return determineOperationOutcome(result);
    }

    /**
     * 根据 Id 批量删除
     */
    @DeleteMapping("/deleteByIdBatch")
    public Result deleteByIdBatch(@RequestParam List<Long> list){
        Integer result = boxFileChunkService.deleteByIdBatch(list);
        return determineOperationOutcome(result);
    }
    /**
     * 根据 IdentifierAndChunkNumberAndCreateUser 查询
     */
    @GetMapping("/selectByIdentifierAndChunkNumberAndCreateUser")
    public Result selectByIdentifierAndChunkNumberAndCreateUser(@RequestParam String identifier,@RequestParam Integer chunkNumber,@RequestParam Long createUser){
        return success(boxFileChunkService.selectByIdentifierAndChunkNumberAndCreateUser(identifier,chunkNumber,createUser));
    }

    /**
     * 根据 IdentifierAndChunkNumberAndCreateUser 修改
     */
    @PutMapping("/updateByIdentifierAndChunkNumberAndCreateUser")
    public Result updateByIdentifierAndChunkNumberAndCreateUser(@RequestBody BoxFileChunk bean,@RequestParam String identifier,@RequestParam Integer chunkNumber,@RequestParam Long createUser){
        Integer result = boxFileChunkService.updateByIdentifierAndChunkNumberAndCreateUser(bean,identifier,chunkNumber,createUser);
        return determineOperationOutcome(result);
    }


    /**
     * 根据 IdentifierAndChunkNumberAndCreateUser 删除
     */
    @DeleteMapping("/deleteByIdentifierAndChunkNumberAndCreateUser")
    public Result deleteByIdentifierAndChunkNumberAndCreateUser(@RequestParam String identifier,@RequestParam Integer chunkNumber,@RequestParam Long createUser){
        Integer result = boxFileChunkService.deleteByIdentifierAndChunkNumberAndCreateUser(identifier,chunkNumber,createUser);
        return determineOperationOutcome(result);
    }

}