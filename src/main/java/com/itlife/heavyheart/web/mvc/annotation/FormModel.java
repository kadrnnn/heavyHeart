package com.itlife.heavyheart.web.mvc.annotation;

import java.lang.annotation.*;

/**
 * @Author kex
 * @Create 2020/8/14 10:08
 * @Description
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormModel {
    String value() default "";
}
