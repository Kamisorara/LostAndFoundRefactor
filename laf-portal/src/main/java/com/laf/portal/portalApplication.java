package com.laf.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动用户前台
 */

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.laf.framwork",
        "com.laf.common",
        "com.laf.security",
        "com.laf.portal"
})
public class portalApplication {
    public static void main(String[] args) {
        SpringApplication.run(portalApplication.class, args);

    }

}