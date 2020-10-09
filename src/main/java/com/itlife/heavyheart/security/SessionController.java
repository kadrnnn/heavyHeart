package com.itlife.heavyheart.security;

import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Author kex
 * @Create 2020/9/2 9:18
 * @Description
 */
@RestController
public class SessionController {
//    @RequestMapping("login")
//    public String login(@RequestBody User user, HttpSession session) {
//        if ("admin".equals(user.getUsername()) && "admin".equals(user.getPassword())) {
//            session.setAttribute("user", user);
//            return "登录成功";
//        }
//        return "账号或密码错误";
//    }

    @RequestMapping("logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "退出成功";
    }
}
