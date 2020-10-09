package com.itlife.heavyheart.redisgo.controller;

import com.itlife.heavyheart.web.result.ResultBean;
import com.itlife.heavyheart.web.result.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author kex
 * @Create 2020/9/10 15:49
 * @Description
 */
@RestController
@RequestMapping("/api/redis")
public class RedisController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/add")
    public ResultBean add() {
        stringRedisTemplate.opsForValue().set("kadrn", "kadrn");
        return ResultUtils.success();
    }

    @GetMapping("/get")
    public ResultBean get() {
        String name = stringRedisTemplate.opsForValue().get("name");
        return ResultUtils.success(name);
    }
}
