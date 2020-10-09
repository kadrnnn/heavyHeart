package com.itlife.heavyheart.interceptor;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author kex
 * @Create 2020/5/25 10:35
 * @Description
 */
public interface TokenService {
    /**
     * 创建token
     * @return
     */
    String createToken();

    /**
     * 检验token
     * @param request
     * @return
     */
    boolean checkToken(HttpServletRequest request) throws Exception;

}
