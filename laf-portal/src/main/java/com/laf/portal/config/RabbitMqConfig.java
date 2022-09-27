package com.laf.portal.config;

import com.laf.common.constant.RabbitMqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitMQ配置类
 */
@Configuration
public class RabbitMqConfig {

    //1.申明注册交换机
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(RabbitMqConstant.EXCHANGE, true, false);
    }


    //2.声明队列 sms.direct.queue
    @Bean
    public Queue smsQueue() {
        return new Queue(RabbitMqConstant.SMS, true);
    }


    @Bean
    public Queue emailQueue() {
        return new Queue(RabbitMqConstant.EMAIL, true);
    }

    @Bean
    public Queue registerVerifyCodeQueue() {
        return new Queue(RabbitMqConstant.REGISTER_VERIFY_CODE_EMAIL, true);
    }


    //3.完成绑定关系（队列和交换机完成绑定关系）
    @Bean
    public Binding smsBinding() {
        return BindingBuilder.bind((smsQueue())) //绑定队列
                .to(directExchange()) //到绑定交换机
                .with(RabbitMqConstant.SMS_ROUTING_KEY); //使用自定义的routingKey
    }


    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind((emailQueue())) //绑定队列
                .to(directExchange()) //到绑定交换机
                .with(RabbitMqConstant.EMAIL_ROUTING_KEY); //使用自定义的routingKey
    }

    @Bean
    public Binding registerVerifyCodeBinding() {
        return BindingBuilder.bind(registerVerifyCodeQueue())
                .to(directExchange())
                .with(RabbitMqConstant.REGISTER_VERIFY_CODE_EMAIL_KEY);
    }


}
