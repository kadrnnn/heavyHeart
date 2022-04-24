package com.itlife.heavyheart.apilimit;

import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

@Aspect
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ApiLimitAspect {
    public static Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    // or RateLimiter
    public static Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.itlife.heavyheart.apilimit.ApiRateLimit)")
    public void pointcut() {
    }

    //@Around("execution(*com.itlife.*.*.controller.*.*(..))")
    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object result = null;
        Semaphore semaphore = null;
        // RateLimiter rateLimiter = null;
        Class<?> clazz = joinPoint.getTarget().getClass();
        String key = getRateLimitKey(clazz, joinPoint.getSignature().getName());
        if (key != null) {
            semaphore = semaphoreMap.get(key);

            // rateLimiter = rateLimiterMap.get(key);
        } else {
            semaphore = semaphoreMap.get("api.defaultLimit");

            // rateLimiter = rateLimiterMap.get("api.defaultLimit");
        }
        try {
            semaphore.acquire();

            // rateLimiter.acquire();
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
        return result;
    }

    private String getRateLimitKey(Class<?> clazz, String name) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(name)) {
                if (method.isAnnotationPresent(ApiRateLimit.class)) {
                    String key = method.getAnnotation(ApiRateLimit.class).configKey();
                    return key;
                }
            }
        }
        return null;
    }
}