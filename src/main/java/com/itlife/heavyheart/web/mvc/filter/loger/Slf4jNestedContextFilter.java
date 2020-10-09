package com.itlife.heavyheart.web.mvc.filter.loger;

import com.itlife.heavyheart.springUtils.SpringContextUtils;
import com.itlife.heavyheart.web.auditorAware.BaseAuditorAware;
import com.itlife.heavyheart.web.auditorAware.Userable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author kex
 * @Create 2020/8/14 10:09
 * @Description
 */
public class Slf4jNestedContextFilter extends AbstractRequestLoggingFilter {
    private static final Logger log = LoggerFactory.getLogger(Slf4jNestedContextFilter.class);

    public Slf4jNestedContextFilter() {
    }

    protected void beforeRequest(HttpServletRequest request, String message) {
        if (log.isDebugEnabled()) {
            log.debug(message);
        }

        MDC.put("MDC", this.createMessage(request, "", ""));
    }

    protected void afterRequest(HttpServletRequest request, String message) {
        MDC.clear();
        if (log.isDebugEnabled()) {
            log.debug(message);
        }

    }

    protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
        StringBuilder msg = new StringBuilder();
        msg.append(prefix);
        msg.append("uri=").append(request.getRequestURI());
        MDC.put("uri", request.getRequestURI());
        if (this.isIncludeClientInfo()) {
            String payload = IpUtils.getIpAddr(request);
            if (StringUtils.hasLength(payload)) {
                msg.append(";client=").append(payload);
                MDC.put("ip", payload);
            }

            try {
                Userable user = (Userable)((BaseAuditorAware) SpringContextUtils.getBean(BaseAuditorAware.class)).getCurrentAuditor().get();
                if (user != null) {
                    msg.append(";user=").append(user.getLoginName());
                    MDC.put("user", user.getLoginName());
                }
            } catch (NoSuchBeanDefinitionException var7) {
                log.warn("No NoSuchBeanDefinitionException BaseAuditorAware ");
            }
        }

        if (this.isIncludeHeaders()) {
            msg.append(";headers=").append((new ServletServerHttpRequest(request)).getHeaders());
            MDC.put("headers", (new ServletServerHttpRequest(request)).getHeaders().toString());
        }

        msg.append(suffix);
        return msg.toString();
    }
}
