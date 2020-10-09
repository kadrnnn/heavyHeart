package com.itlife.heavyheart.security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author kex
 * @Create 2020/9/2 9:27
 * @Description
 */
//@Component
public class LoginFilter {
//extends OncePerRequestFilter
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if ("/login".equals(request.getRequestURI())) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        User user = (User) request.getSession().getAttribute("user");
//        if (user != null) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter writer = response.getWriter();
//        writer.write("请先登录");
//        writer.flush();
//        writer.close();
//    }
}
