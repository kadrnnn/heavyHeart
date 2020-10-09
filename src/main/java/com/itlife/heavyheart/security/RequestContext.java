package com.itlife.heavyheart.security;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author kex
 * @Create 2020/9/2 9:37
 * @Description
 */
public class RequestContext {
    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    public static User getCurrentUser() {
        return (User) getCurrentRequest().getSession().getAttribute("user");
    }
}
