package com.itlife.heavyheart.interceptor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Author kex
 * @Create 2020/5/25 10:35
 * @Description
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private RedisService redisService;


    /**
     * 创建token
     *
     * @return
     */
    @Override
    public String createToken() {
        String str = UUID.randomUUID().toString();
        StrBuilder token = new StrBuilder();
        try {
            token.append("TOKEN_PREFIX").append(str);
            redisService.setEx(token.toString(), token.toString(), 10000L);
            boolean notEmpty = StringUtils.isNotEmpty(token.toString());
            if (notEmpty) {
                return token.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * 检验token
     *
     * @param request
     * @return
     */
    @Override
    public boolean checkToken(HttpServletRequest request) throws Exception {

        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {// header中不存在token
            token = request.getParameter("token");
            if (StringUtils.isBlank(token)) {// parameter中也不存在token
                throw new RuntimeException("错误");
            }
        }

        if (!redisService.exists(token)) {
            throw new RuntimeException("错误");
        }

        boolean remove = redisService.remove(token);
        if (!remove) {
            throw new RuntimeException("错误");
        }
        return true;
    }
}
