package com.laf.admin.controller;


import com.laf.admin.service.sys.LoginService;
import com.laf.framwork.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sso")
@Api(tags = "管理员登录接口", description = "管理员登录接口，匿名访问")
public class SysUserController {


    private static final Logger logger = LoggerFactory.getLogger(SysUserController.class);
    @Autowired
    private LoginService loginService;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ApiOperation("用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult login(@RequestParam("userName") String userName,
                              @RequestParam("password") String password) {
        String token = loginService.login(userName, password);
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        logger.info(userName + "登录了");
        return CommonResult.success(tokenMap);
    }



}
