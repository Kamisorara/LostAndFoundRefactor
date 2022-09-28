package com.laf.admin.service.sys.impl;


import com.laf.admin.dao.UserLoginLogMapper;
import com.laf.admin.dao.UserMapper;
import com.laf.admin.service.UserService;
import com.laf.admin.service.sys.LoginService;
import com.laf.common.entity.sys.User;
import com.laf.common.entity.sys.UserLoginLog;
import com.laf.framwork.exception.Asserts;
import com.laf.framwork.util.JwtUtil;
import com.laf.framwork.util.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Autowired
    private UserLoginLogMapper userLoginLogMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private com.laf.framwork.service.ipService ipService;


    @Override
    public String login(String userName, String password) {
        String token = null;
        try {
            UserDetails userDetails = userService.loadUserByUsername(userName);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            //获取获取用户id
            User user = userMapper.getUserByName(userName);
            //账号是否被暂停使用
            if (user.getStatus().equals("0") && user.getUserType().equals("0")) {
                String userId = user.getId().toString();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                //生成jwt
                token = JwtUtil.createJWT(userId);
                //把完整的用户信息存入redis
                insertLoginLog(Long.parseLong(userId));
                redisCache.setCacheObject("login:" + userId, userDetails);
            } else {
                Asserts.fail("该账号非管理员账号或已被停用");
            }
        } catch (AuthenticationException e) {
            logger.error("{ 登录异常 }" + e.getMessage());
        }
        return token;
    }

    @Override
    public void insertLoginLog(Long userId) {
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setUserId(userId);
        //查询ip地址
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ipAddr = ipService.getUserInquireIpAddress(attributes);
        userLoginLog.setIp(ipAddr);
        userLoginLogMapper.insert(userLoginLog);
    }

}
