package com.laf.portal.service.sys.impl;


import com.laf.common.constant.RabbitMqConstant;
import com.laf.common.entity.sys.Rank;
import com.laf.common.entity.sys.User;
import com.laf.common.entity.sys.UserLoginLog;
import com.laf.common.entity.sys.UserRole;
import com.laf.framwork.exception.Asserts;
import com.laf.framwork.util.JwtUtil;
import com.laf.framwork.util.RedisCache;
import com.laf.portal.dao.UserLoginLogMapper;
import com.laf.portal.dao.UserMapper;
import com.laf.portal.dao.UserRoleMapper;
import com.laf.portal.dao.rankMapper;
import com.laf.portal.service.UserService;
import com.laf.portal.service.sys.LoginService;
import com.laf.portal.service.sys.VerifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    @Autowired
    private VerifyService verifyService;

    @Resource
    private com.laf.framwork.service.ipService ipService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private rankMapper rankMapper;


    @Override
    public String login(String userName, String password) {
        String token = null;
        try {
            UserDetails userDetails = userService.loadUserByUsername(userName);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("???????????????");
            }
            //??????????????????id
            User user = userMapper.getUserByName(userName);
            //???????????????????????????
            if (user.getStatus().equals("0")) {
                String userId = user.getId().toString();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                //??????jwt
                token = JwtUtil.createJWT(userId);
                //??????????????????????????????redis
                insertLoginLog(Long.parseLong(userId));
                redisCache.setCacheObject("login:" + userId, userDetails);
            } else {
                Asserts.fail("?????????????????????");
            }
        } catch (AuthenticationException e) {
            logger.error("{ ???????????? }" + e.getMessage());
        }
        return token;
    }

    @Override
    public void insertLoginLog(Long userId) {
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setUserId(userId);
        //??????ip??????
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ipAddr = ipService.getUserInquireIpAddress(attributes);
        userLoginLog.setIp(ipAddr);
        userLoginLogMapper.insert(userLoginLog);
    }

    @Override
    public String register(String userName,
                           String password,
                           String passwordRepeat,
                           String emailAddr,
                           String emailVerifyCode) {
        //????????????????????????????????????
        if (verifyService.verifyRegisterCode(emailAddr, emailVerifyCode) && password.equals(passwordRepeat)) {
            try {
                User user = new User();
                user.setUserName(userName);
                user.setPassword(passwordEncoder.encode(password));
                user.setEmail(emailAddr);
                userMapper.insert(user);
                //???????????????id???????????????????????????id ??????id?????????????????????????????????
                UserRole userRole = new UserRole();
                Long userIdInDataBase = userMapper.selectUserIdByUserName(userName);
                userRole.setUserId(userIdInDataBase);
                userRole.setRoleId(1L);//????????????????????????????????????????????????
                userRoleMapper.insert(userRole);
                //?????????????????????rank?????????????????????
                Rank rank = new Rank();
                rank.setUserId(userIdInDataBase);
                rank.setHelpTimes(0);
                rankMapper.insert(rank);
                //??????rabbitMQ??????????????????????????????
                rabbitTemplate.convertAndSend(RabbitMqConstant.EXCHANGE, RabbitMqConstant.EMAIL_ROUTING_KEY, emailAddr);
            } catch (Exception e) {
                Asserts.fail("{ ???????????? }" + e.getMessage());
            }
        } else {
            Asserts.fail("{ ???????????? }" + "??????ip??????:" + ipService.getUserInquireIpAddress((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()));
        }
        return "????????????";
    }

}
