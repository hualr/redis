package com.example.redis.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class SpringRedisApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        final String ping = redisTemplate.getConnectionFactory().getConnection().ping();
        System.out.println("ping = " + ping);
    }

    /**
     * 存入数据
     */
    @Test
    public void test1() {
        redisTemplate.opsForValue().set("username", "hha");
        final Object username = redisTemplate.opsForValue().get("username");
        System.out.println("username = " + username);
    }

}
