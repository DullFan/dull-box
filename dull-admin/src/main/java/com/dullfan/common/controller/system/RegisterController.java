package com.dullfan.common.controller.system;

import com.dullfan.common.controller.ABaseController;
import com.dullfan.common.entity.po.RegisterBody;
import com.dullfan.common.entity.vo.Result;
import com.dullfan.common.utils.StringUtils;
import com.dullfan.framework.web.service.RegisterService;
import com.dullfan.system.service.BoxUserFileService;
import com.dullfan.system.service.ConfigService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册验证
 */
@RestController
@Validated
public class RegisterController extends ABaseController {

    @Resource
    private RegisterService registerService;

    @Resource
    private ConfigService configService;


    @PostMapping("/register")
    public Result register(@Valid @RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByConfigKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
