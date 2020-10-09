package com.itlife.heavyheart.web.controller;

import com.itlife.heavyheart.web.mvc.filter.loger.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author kex
 * @Create 2020/8/14 15:41
 * @Description
 */
public abstract class BaseController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    public BaseController() {
    }

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected HttpServletResponse getResponse() {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
    }

    protected String getRemoteIpAddress() {
        return IpUtils.getIpAddr(this.getRequest());
    }

    protected String getCurrnetHostIpAddress() {
        return IpUtils.getCurrnetHostIp();
    }

//    @InitBinder
//    protected void initBinder(WebDataBinder binder) {
//        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
//            public void setAsText(String text) {
//                this.setValue(text == null ? null : StringEscapeUtil.escapeHtml4(text.trim()));
//            }
//
//            public String getAsText() {
//                Object value = this.getValue();
//                return value != null ? value.toString() : "";
//            }
//        });
//    }
}
