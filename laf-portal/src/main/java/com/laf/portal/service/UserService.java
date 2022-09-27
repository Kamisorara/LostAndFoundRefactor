package com.laf.portal.service;


import com.laf.common.entity.sys.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户 Service
 */

public interface UserService {
    /**
     * 根据用户名获取用户
     */
    User getUserByName(String userName);


    /**
     * 获取用户信息 返回UserDetails类给security
     */
    UserDetails loadUserByUsername(String userName);
}
