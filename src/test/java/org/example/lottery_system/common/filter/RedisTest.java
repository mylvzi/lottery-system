package org.example.lottery_system.common.filter;

import org.example.lottery_system.common.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtil redisUtil;
    @Test
    public void redisTest() {
        stringRedisTemplate.opsForValue().set("keu1", "val2");
        System.out.println(stringRedisTemplate.opsForValue().get("keu1"));
    }

    @Test
    void redisUtil() {
        redisUtil.set("key2", "value2");
        redisUtil.set("key3", "value3", 60L);
        System.out.println("has key2:" + redisUtil.hasKey("key2"));
        System.out.println("has key3:" + redisUtil.hasKey("key3"));
        System.out.println("key2:" + redisUtil.get("key2"));
        System.out.println("key3:" + redisUtil.get("key3"));
        redisUtil.del("key2");
        System.out.println("has key2:" + redisUtil.hasKey("key2"));
        System.out.println("has key3:" + redisUtil.hasKey("key3"));
    }
}
