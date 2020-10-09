package com.itlife.heavyheart.interceptor;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author kex
 * @Create 2020/5/25 10:50
 * @Description
 */
@RestController
public class kadrnController {
    @Resource
    private TokenService tokenService;

    @Resource
    private TestService testService;


    @PostMapping("/get/token")
    public String  getToken(){
        String token = tokenService.createToken();
        if (StringUtils.isNotEmpty(token)) {
            return token;
        }
        return "";
    }


    @AutoIdempotent
    @PostMapping("/test/Idempotence")
    public String testIdempotence() {
        String businessResult = testService.testIdempotence();
        if (StringUtils.isNotEmpty(businessResult)) {
            return businessResult;
        }
        return "";
    }
}
