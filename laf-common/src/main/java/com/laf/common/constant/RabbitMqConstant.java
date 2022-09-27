package com.laf.common.constant;

/**
 * rabbitMq 交换机队列名称
 */
public class RabbitMqConstant {

    //交换机名称
    public static final String EXCHANGE = "laf_exchange";

    //消息队列（Queue名称）
    public static final String SMS = "laf.portal.sms.queue";

    public static final String EMAIL = "laf.portal.email.queue";

    public static final String REGISTER_VERIFY_CODE_EMAIL = "portal.register.verify.email";

    //routingKey
    public static final String SMS_ROUTING_KEY = "sms";

    public static final String EMAIL_ROUTING_KEY = "email";

    public static final String REGISTER_VERIFY_CODE_EMAIL_KEY = "verifyEmail";

}
