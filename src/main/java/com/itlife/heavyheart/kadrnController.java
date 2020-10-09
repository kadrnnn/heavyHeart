package com.itlife.heavyheart;

import com.itlife.heavyheart.security.User;
import com.itlife.heavyheart.security.UserContext;
import com.itlife.heavyheart.web.result.ResultBean;
import com.itlife.heavyheart.web.result.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author kex
 * @Create 2020/6/4 15:34
 * @Description
 */
@RestController("/kadrn")
public class kadrnController {
    //@Autowired
    //FeignService feignService;

    //@GetMapping("hello")
    //public String kadrn() {
        //return feignService.mark("1");
    //}

    @GetMapping("hello1")
    public ResultBean kadrn1() {
        return ResultUtils.success(UserContext.getCurrentUserName());
    }

    @PostMapping("test")
    public ResultBean kadrn2(@RequestBody User user) {
        return ResultUtils.success("1");
    }
}
