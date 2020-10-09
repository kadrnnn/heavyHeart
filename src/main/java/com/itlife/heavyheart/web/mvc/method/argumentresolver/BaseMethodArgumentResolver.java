package com.itlife.heavyheart.web.mvc.method.argumentresolver;

import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Collections;
import java.util.Map;

/**
 * @Author kex
 * @Create 2020/8/14 10:19
 * @Description
 */
public abstract class BaseMethodArgumentResolver implements HandlerMethodArgumentResolver {
    public BaseMethodArgumentResolver() {
    }

    private boolean illegalChar(char ch) {
        return ch != '.' && ch != '_' && (ch < '0' || ch > '9');
    }

    protected final Map<String, String> getUriTemplateVariables(NativeWebRequest request) {
        Map<String, String> variables = (Map)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, 0);
        return variables != null ? variables : Collections.emptyMap();
    }
}
