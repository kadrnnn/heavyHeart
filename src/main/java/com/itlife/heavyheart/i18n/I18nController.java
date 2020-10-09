package com.itlife.heavyheart.i18n;

import com.itlife.heavyheart.web.result.ResultBean;
import com.itlife.heavyheart.web.result.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author kex
 * @Create 2020/7/14 14:36
 * @Description
 */
@RestController
@RequestMapping("/i18n")
public class I18nController {
    //    @RequestMapping("/user")
//    public String getUsername() {
//        return MessageUtils.get("username");
//    }
    @RequestMapping("/user")
    public ResultBean getUsername() {
        return ResultUtils.success(MessageUtils.get("username"));
    }
}
