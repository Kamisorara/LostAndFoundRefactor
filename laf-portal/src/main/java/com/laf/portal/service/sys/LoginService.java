package com.laf.portal.service.sys;

import org.springframework.transaction.annotation.Transactional;

/**
 * 用户登录注册Service
 */
public interface LoginService {

    //用户登录(返回token)
    String login(String userName, String password);

    //用户登录Log记录
    void insertLoginLog(Long userId);

    //用户注册
    @Transactional
    String register(String userName,
                    String password,
                    String passwordRepeat,
                    String emailAddr,
                    String emailVerifyCode);
}
