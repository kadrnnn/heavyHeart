package com.itlife.heavyheart.apilimit;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

@Component
public class InitApiLimitRateListener implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Environment environment = ctx.getEnvironment();
        String defaultLimit = environment.getProperty("api.defaultLimit");
        Object rate = defaultLimit == null ? 100 : defaultLimit;
        ApiLimitAspect.semaphoreMap.put("api.defaultLimit", new Semaphore(Integer.parseInt(rate.toString())));

        ApiLimitAspect.rateLimiterMap.put("api.defaultLimit", RateLimiter.create(Double.parseDouble(rate.toString())));
        Map<String, Object> beanMap = ctx.getBeansWithAnnotation(RestController.class);
        Set<String> keys = beanMap.keySet();
        for (String key : keys) {
            Class<?> clazz = beanMap.get(key).getClass();
            String fullName = beanMap.get(key).getClass().getName();
            if (fullName.contains("EnhancerBySpringCGLIB") || fullName.contains("$$")) {
                fullName = fullName.substring(0, fullName.indexOf("$$"));
                try {
                    clazz = Class.forName(fullName);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if(method.isAnnotationPresent(ApiRateLimit.class)){
                    String apiKey = method.getAnnotation(ApiRateLimit.class).configKey();
                    String property = environment.getProperty(apiKey);
                    if (property != null) {
                        int limit = Integer.parseInt(property);
                        ApiLimitAspect.semaphoreMap.put(apiKey, new Semaphore(limit));

                        ApiLimitAspect.rateLimiterMap.put(apiKey, RateLimiter.create(Double.parseDouble(property)));
                    }
                }
            }
        }
    }
}
