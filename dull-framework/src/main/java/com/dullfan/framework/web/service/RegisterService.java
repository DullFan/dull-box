package com.dullfan.framework.web.service;

import com.dullfan.common.constant.FileConstants;
import com.dullfan.common.constant.UserConstants;
import com.dullfan.common.core.redis.RedisCache;
import com.dullfan.common.entity.po.RegisterBody;
import com.dullfan.common.entity.po.User;
import com.dullfan.common.utils.SecurityUtils;
import com.dullfan.common.utils.StringUtils;
import com.dullfan.system.entity.po.BoxUserFile;
import com.dullfan.system.service.BoxUserFileService;
import com.dullfan.system.service.ConfigService;
import com.dullfan.system.service.FileService;
import com.dullfan.system.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 注册校验方法
 *
 * @author ruoyi
 */
@Component
public class RegisterService {
    @Resource
    private UserService userService;

    @Resource
    private FileService fileService;

    /**
     * 注册
     */
    @Transactional
    public String register(RegisterBody registerBody) {
        String msg = "";
        String username = registerBody.getUsername(), password = registerBody.getPassword();
        User sysUser = new User();
        sysUser.setUserName(username);
        if (!userService.checkUserNameUnique(sysUser)) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            sysUser.setUserName(registerBody.getUsername());
            Integer add = userService.add(sysUser);

            if (add <= 0) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                BoxUserFile boxUserFile = new BoxUserFile();
                boxUserFile.setParentId(FileConstants.TOP_PARENT_ID);
                boxUserFile.setUserId(sysUser.getUserId());
                boxUserFile.setFilename(FileConstants.ALL_FILE_CN_STR);
                fileService.insert(boxUserFile);
            }
        }

        return msg;
    }
}
