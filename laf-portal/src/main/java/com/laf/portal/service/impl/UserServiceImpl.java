package com.laf.portal.service.impl;

import com.laf.common.entity.sys.User;
import com.laf.portal.dao.MenuMapper;
import com.laf.portal.dao.UserMapper;
import com.laf.portal.service.UserService;
import com.laf.security.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 用户相关Service
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public User getUserByName(String userName) {
        return userMapper.getUserByName(userName);
    }


    @Override
    public UserDetails loadUserByUsername(String userName) {
        User user = getUserByName(userName);
        //如果没有找到该用户就抛出异常
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名或者密码错误");
        }
        //用户权限列表
        List<String> permsList = menuMapper.selectPermsByUserId(user.getId());
        return new LoginUser(user, permsList);
    }
}
