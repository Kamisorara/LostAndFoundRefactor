package com.laf.admin.service.sys;

import org.springframework.transaction.annotation.Transactional;

/**
 * 用户登录注册Service
 */
public interface LoginService {

    //用户登录(返回token)
    String login(String userName, String password);

    //用户登录Log记录
    void insertLoginLog(Long userId);

}
