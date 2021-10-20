package com.itlife.heavyheart.apilimit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiLimitConfiguration {
    @Bean
    public ApiLimitAspect apiLimitAspect(){
        return new ApiLimitAspect();
    }
}
