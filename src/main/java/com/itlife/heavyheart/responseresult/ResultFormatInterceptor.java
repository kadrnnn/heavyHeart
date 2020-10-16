package com.itlife.heavyheart.responseresult;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author kex
 * @Create 2020/10/12 16:29
 * @Description
 */
@Slf4j
@Component
public class ResultFormatInterceptor implements HandlerInterceptor {
    // 标记名称
    public static final String RESULT_FORMAT = "RESULT_FORMAT";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 请求的方法
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Class<?> clazz = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();
            // 判断是否在类对象上加@ResultFormat
            // 设置此请求返回体，需要包装，往下传递，在ResponseBodyAdvice接口进行判断
            if (clazz.isAnnotationPresent(ResultFormat.class)) {
                request.setAttribute(RESULT_FORMAT, clazz.getAnnotation(ResultFormat.class));
            } else if (method.isAnnotationPresent(ResultFormat.class)) {
                request.setAttribute(RESULT_FORMAT, method.getAnnotation(ResultFormat.class));
            }
        }
        return true;
    }
}
