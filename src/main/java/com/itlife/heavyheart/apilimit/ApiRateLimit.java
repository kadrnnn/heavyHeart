package com.itlife.heavyheart.apilimit;

import java.lang.annotation.*;

/**
 * 对 API 进行访问速度限制
 * 限制的速度值在Apollo配置中通过 key 关联
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiRateLimit {
    /**
     * Nacos配置中的 key
     *
     * @return
     */
    String configKey();
}
