package com.itlife.heavyheart.web.mvc.method.argumentresolver;

import com.itlife.heavyheart.web.mvc.annotation.RequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author kex
 * @Create 2020/8/14 10:32
 * @Description
 */
public class RequestModelMethodArgumentResolver extends BaseMethodArgumentResolver {
    private static final Logger log = LoggerFactory.getLogger(RequestModelMethodArgumentResolver.class);
    private static final JsonModelMethodArgumentResolver DEFAULT_JSONMODEL_RESOLVER = new JsonModelMethodArgumentResolver();
    private static final FormModelMethodArgumentResolver DEFAULT_FORMMODEL_RESOLVER = new FormModelMethodArgumentResolver();
    private JsonModelMethodArgumentResolver jsonResolver;
    private FormModelMethodArgumentResolver formResolver;

    public RequestModelMethodArgumentResolver() {
        this.jsonResolver = DEFAULT_JSONMODEL_RESOLVER;
        this.formResolver = DEFAULT_FORMMODEL_RESOLVER;
    }

    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(RequestModel.class);
    }

    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest)nativeWebRequest.getNativeRequest();
        String contentType = request.getContentType();
        return null != contentType && contentType.contains("application/json") ? this.jsonResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory) : this.formResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory);
    }
}
