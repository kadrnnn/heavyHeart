package com.itlife.heavyheart.i18n;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * @Author kex
 * @Create 2020/7/14 9:48
 * @Description
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 默认拦截器 其中lang表示切换语言的参数名
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }
}
