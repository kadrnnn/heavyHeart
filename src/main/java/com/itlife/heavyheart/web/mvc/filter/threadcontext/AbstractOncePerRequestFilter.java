package com.itlife.heavyheart.web.mvc.filter.threadcontext;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author kex
 * @Create 2020/8/14 10:16
 * @Description
 */
public abstract class AbstractOncePerRequestFilter extends OncePerRequestFilter {
    public AbstractOncePerRequestFilter() {
    }

    protected abstract boolean beforeRequest(HttpServletRequest var1, HttpServletResponse var2);

    protected abstract void afterRequest(HttpServletRequest var1, HttpServletResponse var2);

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (this.beforeRequest(request, response)) {
            try {
                chain.doFilter(request, response);
            } finally {
                this.afterRequest(request, response);
            }
        }

    }
}
