package com.dullfan.framework.web.service;

import com.alibaba.fastjson2.JSONObject;
import com.dullfan.common.constant.FileConstants;
import com.dullfan.common.entity.po.LoginUser;
import com.dullfan.common.entity.po.User;
import com.dullfan.common.entity.vo.UserVo;
import com.dullfan.common.enums.DelFlagEnum;
import com.dullfan.common.enums.FolderFlagEnum;
import com.dullfan.common.enums.UserStatusEnum;
import com.dullfan.common.exception.ServiceException;
import com.dullfan.common.utils.StringUtils;
import com.dullfan.system.entity.po.BoxUserFile;
import com.dullfan.system.entity.query.BoxUserFileQuery;
import com.dullfan.system.service.BoxUserFileService;
import com.dullfan.system.service.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.util.List;

@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;

    @Resource
    private PasswordService passwordService;

    @Resource
    private BoxUserFileService boxUserFileService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;

        user = userService.selectUserByUserName(username);


        if (StringUtils.isNull(user)) {
            throw new ServiceException("用户不存在或密码错误");
        } else if (UserStatusEnum.DELETED.getCode().equals(user.getDelFlag())) {
            throw new ServiceException("用户已被删除");
        } else if (UserStatusEnum.DISABLE.getCode().equals(user.getStatus())) {
            throw new ServiceException("用户已被禁用");
        }

        passwordService.validate(user);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        BoxUserFileQuery boxUserFileQuery = new BoxUserFileQuery();
        boxUserFileQuery.setUserId(user.getUserId());
        boxUserFileQuery.setParentId(FileConstants.TOP_PARENT_ID);
        boxUserFileQuery.setDelFlag(DelFlagEnum.NO.getCode());
        boxUserFileQuery.setFolderFlag(FolderFlagEnum.YES.getCode());
        List<BoxUserFile> boxUserFiles = boxUserFileService.selectListByParam(boxUserFileQuery);
        if (!boxUserFiles.isEmpty()) {
            BoxUserFile boxUserFile = boxUserFiles.get(0);
            userVo.setRootFileId(boxUserFile.getFileId());
            userVo.setRootFilename(boxUserFile.getFilename());
        }
        return createLoginUser(userVo);
    }

    public UserDetails createLoginUser(UserVo user) {
        return new LoginUser(user.getUserId(), user);
    }
}
