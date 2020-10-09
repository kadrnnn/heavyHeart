package com.itlife.heavyheart.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Date;

/**
 * @Author kex
 * @Create 2020/9/2 9:47
 * @Description
 */
public final class JwtUtil {
    public final static Logger log = LoggerFactory.getLogger(JwtUtil.class);
    //密钥
    public final static String SECRETKEY = "kadrn";
    public final static Duration EXPIRATION = Duration.ofHours(2);

    public static String generate(String username) {
        //过期时间
        Date expiryDate = new Date(System.currentTimeMillis() + EXPIRATION.toMillis());
        return Jwts.builder()
                .setSubject(username) // 将userName放进JWT
                .setIssuedAt(new Date()) // 设置JWT签发时间
                .setExpiration(expiryDate) // 设置过期时间
                .signWith(SignatureAlgorithm.HS512, SECRETKEY) // 设置加密算法和秘钥
                .compact();
    }

    public static Claims parse(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRETKEY) // 设置密钥
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.info("token解析失败");
        }
        return claims;
    }
}
