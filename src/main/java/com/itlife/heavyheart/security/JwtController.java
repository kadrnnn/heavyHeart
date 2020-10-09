package com.itlife.heavyheart.security;

import org.springframework.web.bind.annotation.*;


/**
 * @Author kex
 * @Create 2020/9/2 10:16
 * @Description
 */
@RestController
public class JwtController {

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        if ("admin".equals(user.getUsername()) && "admin".equals(user.getPassword())) {
            return JwtUtil.generate(user.getUsername());
        }
        return "账号密码错误";
    }
}
