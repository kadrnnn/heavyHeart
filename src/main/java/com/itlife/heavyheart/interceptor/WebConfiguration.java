package com.itlife.heavyheart.interceptor;

import com.itlife.heavyheart.security.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Author kex
 * @Create 2020/5/25 10:38
 * @Description
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Resource
    private AutoIdempotentInterceptor autoIdempotentInterceptor;
    @Resource
    private LoginInterceptor loginInterceptor;

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(autoIdempotentInterceptor);
        registry.addInterceptor(loginInterceptor);
    }
}
