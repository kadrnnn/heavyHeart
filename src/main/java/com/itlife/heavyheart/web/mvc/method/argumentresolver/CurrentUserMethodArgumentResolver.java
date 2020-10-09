package com.itlife.heavyheart.web.mvc.method.argumentresolver;

import com.itlife.heavyheart.springUtils.SpringContextUtils;
import com.itlife.heavyheart.web.auditorAware.BaseAuditorAware;
import com.itlife.heavyheart.web.mvc.annotation.CurrentUser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author kex
 * @Create 2020/8/14 10:19
 * @Description
 */
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger log = LoggerFactory.getLogger(CurrentUserMethodArgumentResolver.class);

    public CurrentUserMethodArgumentResolver() {
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        CurrentUser currentUserAnnotation = (CurrentUser)parameter.getParameterAnnotation(CurrentUser.class);
        if (StringUtils.isEmpty(currentUserAnnotation.value())) {
            return this.getCurrentAuditor();
        } else {
            Object retVal = null;
            HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
            retVal = request.getAttribute(currentUserAnnotation.value());
            if (retVal == null && request.getSession(false) != null) {
                retVal = request.getSession(false).getAttribute(currentUserAnnotation.value());
            }

            if (retVal == null) {
                retVal = this.getCurrentUserByUserVariable(currentUserAnnotation.value());
            }

            return retVal;
        }
    }

    private Object getCurrentUserByUserVariable(String variableKey) {
        BaseAuditorAware auditorAware = (BaseAuditorAware) SpringContextUtils.getBean(BaseAuditorAware.class);
        if (auditorAware != null) {
            return "UserVariable".equalsIgnoreCase(variableKey) ? auditorAware : auditorAware.getCurrentAuditor().get();
        } else {
            return null;
        }
    }

    private Object getCurrentAuditor() {
        try {
            BaseAuditorAware auditorAware = (BaseAuditorAware)SpringContextUtils.getBean(BaseAuditorAware.class);
            return auditorAware.getCurrentAuditor();
        } catch (NoSuchBeanDefinitionException var2) {
            log.warn("获取AuditorBean异常.{}", var2.getMessage());
            return null;
        }
    }
}
