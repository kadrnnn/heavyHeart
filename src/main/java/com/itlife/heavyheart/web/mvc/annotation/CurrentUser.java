package com.itlife.heavyheart.web.mvc.annotation;

import java.lang.annotation.*;

/**
 * @Author kex
 * @Create 2020/8/14 10:07
 * @Description
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
    String value() default "";
}
