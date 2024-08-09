package com.dullfan.common.controller.box;

import java.util.Date;
import java.util.List;

import com.dullfan.common.controller.ABaseController;
import com.dullfan.system.entity.po.BoxShare;
import com.dullfan.system.entity.query.BoxShareQuery;
import com.dullfan.system.service.BoxShareService;
import com.dullfan.common.entity.vo.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController("BoxShareController")
@RequestMapping("/boxShare")
public class BoxShareController extends ABaseController {

    @Resource
    BoxShareService boxShareService;

    /**
     * 分页查询
     */
    @GetMapping("/loadDataList")
    public Result selectListByPage(BoxShareQuery param){
        return success(boxShareService.selectListByPage(param));
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody BoxShare bean){
        Integer result = boxShareService.insert(bean);
        return determineOperationOutcome(result);
    }

    /**
     * 批量新增
     */
    @PostMapping("/insertBatch")
    public Result insertBatch(@RequestBody List<BoxShare> listBean){
        Integer result = boxShareService.insertBatch(listBean);
        return determineOperationOutcome(result);
    }

    /**
     * 根据 ShareId 查询
     */
    @GetMapping("/selectByShareId")
    public Result selectByShareId(@RequestParam Long shareId){
        return success(boxShareService.selectByShareId(shareId));
    }

    /**
     * 根据 ShareId 修改
     */
    @PutMapping("/updateByShareId")
    public Result updateByShareId(@RequestBody BoxShare bean,@RequestParam Long shareId){
        Integer result = boxShareService.updateByShareId(bean,shareId);
        return determineOperationOutcome(result);
    }


    /**
     * 根据 ShareId 删除
     */
    @DeleteMapping("/deleteByShareId")
    public Result deleteByShareId(@RequestParam Long shareId){
        Integer result = boxShareService.deleteByShareId(shareId);
        return determineOperationOutcome(result);
    }

    /**
     * 根据 CreateUserAndCreateTime 查询
     */
    @GetMapping("/selectByCreateUserAndCreateTime")
    public Result selectByCreateUserAndCreateTime(@RequestParam Long createUser,@RequestParam Date createTime){
        return success(boxShareService.selectByCreateUserAndCreateTime(createUser,createTime));
    }

    /**
     * 根据 CreateUserAndCreateTime 修改
     */
    @PutMapping("/updateByCreateUserAndCreateTime")
    public Result updateByCreateUserAndCreateTime(@RequestBody BoxShare bean,@RequestParam Long createUser,@RequestParam Date createTime){
        Integer result = boxShareService.updateByCreateUserAndCreateTime(bean,createUser,createTime);
        return determineOperationOutcome(result);
    }


    /**
     * 根据 CreateUserAndCreateTime 删除
     */
    @DeleteMapping("/deleteByCreateUserAndCreateTime")
    public Result deleteByCreateUserAndCreateTime(@RequestParam Long createUser,@RequestParam Date createTime){
        Integer result = boxShareService.deleteByCreateUserAndCreateTime(createUser,createTime);
        return determineOperationOutcome(result);
    }

}