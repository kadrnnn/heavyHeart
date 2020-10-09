package com.itlife.heavyheart.web.mvc.filter.threadcontext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author kex
 * @Create 2020/8/14 10:17
 * @Description
 */
public class ThreadContext {
    private static final ThreadLocal<Map<String, Object>> CTX_HOLDER = new ThreadLocal();

    public ThreadContext() {
    }

    public static final void putContext(String key, Object value) {
        Map<String, Object> ctx = (Map)CTX_HOLDER.get();
        if (null == ctx) {
            init();
            ctx = (Map)CTX_HOLDER.get();
        }

        ctx.put(key, value);
    }

    public static final <T> T getContext(String key) {
        Map<String, Object> ctx = (Map)CTX_HOLDER.get();
        return ctx == null ? null : (T) ctx.get(key);
    }

    public static final Map<String, Object> getContext() {
        Map<String, Object> ctx = (Map)CTX_HOLDER.get();
        return ctx == null ? null : ctx;
    }

    public static final void remove(String key) {
        Map<String, Object> ctx = (Map)CTX_HOLDER.get();
        if (ctx != null) {
            ctx.remove(key);
        }

    }

    public static final boolean contains(String key) {
        Map<String, Object> ctx = (Map)CTX_HOLDER.get();
        return ctx != null ? ctx.containsKey(key) : false;
    }

    public static final void clean() {
        CTX_HOLDER.remove();
    }

    public static final void init() {
        CTX_HOLDER.set(new HashMap());
    }

    public static final void putTraceId(String traceId) {
        putContext("traceId", traceId);
    }

    public static final String getTraceId() {
        return (String)getContext("traceId");
    }

    public static final void putUserId(Integer userId) {
        putContext("userId", userId);
    }

    public static final int getUserId() {
        Integer val = (Integer)getContext("userId");
        return val == null ? 0 : val;
    }

    public static final void putUserName(String userName) {
        putContext("userName", userName);
    }

    public static final String getUserName() {
        return (String) Optional.ofNullable(getContext("userName")).map((name) -> {
            return String.valueOf(name);
        }).orElse("");
    }

    public static final String getClientIp() {
        return (String)getContext("clientIp");
    }

    public static final void putClientIp(String ip) {
        putContext("clientIp", ip);
    }

    public static void putSessionId(String token) {
        putContext("token", token);
    }

    public static String getSessionId() {
        return (String)getContext("token");
    }

    public static final void removeSessionId() {
        remove("token");
    }
}
