package com.laf.portal;

import com.laf.framwork.util.RedisCache;
import com.laf.security.entity.LoginUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class portalTest {
    @Resource
    private RedisCache redisCache;

    @Test
    void getUser() {
        LoginUser loginUser = redisCache.getCacheObject("login:1540533503228669954");
        System.out.println(loginUser.getUsername());
    }
}
