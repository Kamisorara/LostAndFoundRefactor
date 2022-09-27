package com.laf.portal.service.sys.impl;


import com.laf.common.constant.RabbitMqConstant;
import com.laf.framwork.exception.Asserts;
import com.laf.framwork.util.RedisCache;
import com.laf.portal.service.sys.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private RedisCache redisCache;

    //邮件发送者
    @Value("${spring.mail.username}")
    private String from;

    @RabbitHandler
    @RabbitListener(queues = {RabbitMqConstant.EMAIL})
    @Override
    public void sendRegisterSuccessMail(String mailAddr) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("【LostAndFound】注册成功");
            message.setText("您的邮箱账号" + mailAddr + "\n已在ka-mall注册成功" + "\n如非本人操作请发送邮件至kamisola2020@163.com处理");
            message.setTo(mailAddr);
            message.setFrom(from);
            mailSender.send(message);
        } catch (Exception e) {
            Asserts.fail("{邮件异常}" + e.getMessage());
        }

    }

    @Override
    public void sendVerifyCode(String mail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("【LostAndFound】您的验证码为");
            Random random = new Random();
            int code = random.nextInt(89999) + 10000;
            redisCache.setCacheObject("verify:code:" + mail, code, 3, TimeUnit.MINUTES);
            message.setText("您的注册验证码为: " + code + "\n三分钟内有效!\n请不要回复！");
            message.setTo(mail);
            message.setFrom(from);
            mailSender.send(message);
        } catch (Exception e) {
            Asserts.fail("{邮件异常}" + e.getMessage());
        }

    }
}
