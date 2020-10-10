package com.itlife.heavyheart.project.controller;

import com.itlife.heavyheart.FeignService;
import com.itlife.heavyheart.nettygo.client.NettyClientHandler;
import com.itlife.heavyheart.project.model.Member;
import com.itlife.heavyheart.project.model.UserInfo;
import com.itlife.heavyheart.project.service.AdminService;
import com.itlife.heavyheart.security.JwtUtil;
import com.itlife.heavyheart.security.User;
import com.itlife.heavyheart.web.result.ResultBean;
import com.itlife.heavyheart.web.result.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author kex
 * @Create 2020/9/2 16:36
 * @Description
 */
@RestController
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    @Resource
    AdminService adminService;
    @Resource
    FeignService feignService;

    @PostMapping("/success")
    public ResultBean login(@RequestBody Map<String, Object> map) {
        String kadrn = feignService.mark("2");
        User user = adminService.login(map.get("username").toString(), map.get("password").toString());
        if(user != null){
            return ResultUtils.success(JwtUtil.generate(user.getUsername()));
        }
        return ResultUtils.error("-1","账号或密码错误，请重新登录");
    }

    @PostMapping("/setup")
    public ResultBean setup(@RequestBody Map<String, Object> map) {
        if (!map.isEmpty()) {
            UserInfo userInfo = new UserInfo();
            User user = new User();
            String id = UUID.randomUUID().toString().replaceAll("-", "").trim();
            userInfo.setName(map.get("name").toString());
            userInfo.setTelephone(map.get("telephone").toString());
            userInfo.setUserId(id);
            user.setId(id);
            user.setUsername(map.get("username").toString());
            user.setPassword(map.get("password").toString());
            String check = adminService.checkUser(map.get("username").toString(), map.get("password").toString());
            if ("已注册".equals(check)) {
                return ResultUtils.error("-1", "该用户已注册，请重新登录");
            }
            return ResultUtils.success(adminService.setup(userInfo, user));
        }
        return ResultUtils.error("-1", "失败");
    }

    @PostMapping("/saveInfo")
    public ResultBean saveInfo(@RequestBody Map<String, Object> map) {
        List<Map<String, Object>> listRyxx = (List<Map<String, Object>>) map.get("map");
        return ResultUtils.success(adminService.saveMember(listRyxx));
    }

    @PostMapping("/searchMember")
    public ResultBean searchMember() {
        String idCard = "330125197403035816";
        List<Member> member = adminService.searchMember(idCard);
        return ResultUtils.success(member);
    }

    @PostMapping("/userinfo")
    public ResultBean userinfo(@RequestBody Map<String,Object> username) {
        User user = adminService.userinfo(username.get("username").toString());
        return ResultUtils.success(user);
    }
}
