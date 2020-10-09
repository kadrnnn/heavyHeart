package com.itlife.heavyheart;

import org.springframework.stereotype.Component;

/**
 * @Author kex
 * @Create 2020/9/16 16:03
 * @Description
 */
@Component
public class FeignServiceFallback implements FeignService {
    @Override
    public String mark(String id) {
        return "fallback";
    }
}
