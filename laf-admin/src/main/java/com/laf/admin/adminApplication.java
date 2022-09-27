package com.laf.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动后台管理系统
 */

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.laf.framwork",
        "com.laf.common",
        "com.laf.security",
        "com.laf.admin"
})
public class adminApplication {
    public static void main(String[] args) {
        SpringApplication.run(adminApplication.class, args);
    }

}
