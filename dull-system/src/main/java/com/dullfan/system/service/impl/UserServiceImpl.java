package com.dullfan.system.service.impl;

import java.util.List;

import com.dullfan.common.constant.UserConstants;
import com.dullfan.common.entity.query.SimplePage;
import com.dullfan.common.entity.vo.PaginationResultVo;
import com.dullfan.common.enums.PageSizeEnum;
import com.dullfan.common.entity.po.User;
import com.dullfan.common.entity.query.UserQuery;
import com.dullfan.common.utils.DateUtils;
import com.dullfan.common.utils.SecurityUtils;
import com.dullfan.common.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Service;
import com.dullfan.system.mappers.UserMapper;
import com.dullfan.system.service.UserService;

/**
 * 用户信息表 业务接口实现
 *
 * @author DullFan
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<User> selectListByParam(UserQuery param) {
        return this.userMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer selectCountByParam(UserQuery param) {
        return this.userMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVo<User> selectListByPage(UserQuery param) {
        int count = this.selectCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNum(), count, pageSize);
        param.setSimplePage(page);
        List<User> list = this.selectListByParam(param);
        PaginationResultVo<User> result = new PaginationResultVo(count, page.getPageSize(), page.getPageNum(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(User bean) {
        return this.userMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<User> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userMapper.insertBatch(listBean);
    }

    /**
     * 根据UserId获取对象
     */
    @Override
    public User selectUserByUserId(Long userId) {
        return this.userMapper.selectByUserId(userId);
    }

    /**
     * 根据UserId修改
     */
    @Override
    public Integer updateUserByUserId(User bean, Long userId) {
        UserQuery userQuery = new UserQuery();
        userQuery.setUserId(userId);
        bean.setUpdateBy(SecurityUtils.getUserId().toString());
        bean.setUpdateTime(DateUtils.getNowDate());
        return this.userMapper.updateByParam(bean, userQuery);
    }

    @Override
    public Integer updateUser(User bean) {
        UserQuery userQuery = new UserQuery();
        userQuery.setUserId(bean.getUserId());
        return userMapper.updateByParam(bean, userQuery);
    }

    /**
     * 根据UserId删除
     */
    @Override
    public Integer deleteUserByUserId(Long userId) {
        return this.userMapper.deleteByUserId(userId);
    }

    /**
     * 根据ID批量删除
     */
    @Override
    public Integer deleteUserByUserIdBatch(List<Long> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return this.userMapper.deleteByUserIdBatch(list);
    }

    /**
     * 根据UserName获取对象
     */
    @Override
    public User selectUserByUserName(String userName) {
        return this.userMapper.selectByUserName(userName);
    }

    /**
     * 根据UserName修改
     */
    @Override
    public Integer updateUserByUserName(User bean, String userName) {
        UserQuery userQuery = new UserQuery();
        userQuery.setUserName(userName);
        bean.setUpdateBy(SecurityUtils.getUserId().toString());
        bean.setUpdateTime(DateUtils.getNowDate());
        return this.userMapper.updateByParam(bean, userQuery);
    }

    /**
     * 根据UserName删除
     */
    @Override
    public Integer deleteUserByUserName(String userName) {
        return this.userMapper.deleteByUserName(userName);
    }

    @Override
    public boolean checkUserNameUnique(User user) {
        long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        User info = userMapper.selectByUserName(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId() != userId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
