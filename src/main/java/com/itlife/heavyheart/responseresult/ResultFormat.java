package com.itlife.heavyheart.responseresult;

import java.lang.annotation.*;

/**
 * @Author kex
 * @Create 2020/10/12 16:26
 * @Description
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResultFormat {
}
