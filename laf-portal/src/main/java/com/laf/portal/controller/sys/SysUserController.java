package com.laf.portal.controller.sys;


import com.laf.common.constant.RabbitMqConstant;
import com.laf.framwork.api.CommonResult;
import com.laf.portal.service.sys.EmailService;
import com.laf.portal.service.sys.LoginService;
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

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sso")
@Api(tags = "SysUserController用户接口", description = "用户登录注册相关接口，匿名访问")
public class SysUserController {


    private static final Logger logger = LoggerFactory.getLogger(SysUserController.class);
    @Autowired
    private LoginService loginService;

    @Autowired
    private EmailService emailService;


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


    @ApiOperation(value = "用户注册", tags = "开放权限")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public CommonResult register(@RequestParam("userName") String userName,
                                 @RequestParam("password") String password,
                                 @RequestParam("passwordRepeat") String passwordRepeat,
                                 @RequestParam("emailAddr") String emailAddr,
                                 @RequestParam("emailVerifyCode") String emailVerifyCode) {
        String resultResponse = loginService.register(userName, password, passwordRepeat, emailAddr, emailVerifyCode);
        return CommonResult.success(resultResponse);
    }

    @ApiOperation(value = "发送注册邮件信息验证码", tags = "开放权限")
    @RequestMapping(value = "/verify-code-mail", method = RequestMethod.POST)
    public CommonResult getRegisterVerifyCode(@RequestParam("emailAddr") String emailAddr) {
        rabbitTemplate.convertAndSend(RabbitMqConstant.EXCHANGE,RabbitMqConstant.REGISTER_VERIFY_CODE_EMAIL_KEY, emailAddr);
        return CommonResult.success("邮件发送成功");
    }

}
