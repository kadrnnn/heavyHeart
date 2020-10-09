package com.itlife.heavyheart.web.mvc.filter.threadcontext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author kex
 * @Create 2020/8/14 10:15
 * @Description
 */
public class ThreadContextNestedFilter extends AbstractOncePerRequestFilter {
    public ThreadContextNestedFilter() {
    }

    protected boolean beforeRequest(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    protected void afterRequest(HttpServletRequest request, HttpServletResponse response) {
        ThreadContext.clean();
    }
}
