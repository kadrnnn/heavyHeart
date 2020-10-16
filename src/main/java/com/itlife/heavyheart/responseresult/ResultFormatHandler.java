package com.itlife.heavyheart.responseresult;

import com.itlife.heavyheart.exception.BusinessException;
import com.itlife.heavyheart.web.result.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author kex
 * @Create 2020/10/12 16:44
 * @Description
 */
@Slf4j
@ControllerAdvice
public class ResultFormatHandler implements ResponseBodyAdvice<Object> {
    // 标记名称
    public static final String RESULT_FORMAT = "RESULT_FORMAT";

    // 是否请求包含了@ResultFormat,没有就直接返回
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        ResultFormat resultFormat = (ResultFormat) request.getAttribute(RESULT_FORMAT);
        return resultFormat == null ? false : true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.info("进入返回体，重写格式中");
        if(body instanceof BusinessException){
            log.info("异常处理");
            BusinessException exception = (BusinessException) body;
            return ResultUtils.error(exception.getCode(),exception.getMessage());
        }
        return ResultUtils.success(body);
    }
}
