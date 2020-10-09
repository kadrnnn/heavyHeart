package com.itlife.heavyheart.security;

/**
 * @Author kex
 * @Create 2020/9/2 10:57
 * @Description
 */
public class UserContext {
    private static final ThreadLocal<String> USER = new ThreadLocal<String>();

    public static void add(String username){
        USER.set(username);
    }

    public static void remove() {
        USER.remove();
    }

    /**
     * @return 当前登录用户的用户名
     */
    public static String getCurrentUserName() {
        return USER.get();
    }
}
