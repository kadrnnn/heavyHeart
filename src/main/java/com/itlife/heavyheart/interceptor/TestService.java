package com.itlife.heavyheart.interceptor;

import org.springframework.stereotype.Service;

/**
 * @Author kex
 * @Create 2020/5/25 11:00
 * @Description
 */
@Service
public class TestService {
    @AutoIdempotent
    public String testIdempotence() {
        return "hi,kadrn";
    }
}
