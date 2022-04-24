package com.itlife.heavyheart.singleton;

import sun.misc.Unsafe;

public class Singleton {
    public static Singleton singleton;
    public static final Unsafe UNSAFE = UnsafeUtil.getUnsafe();
    int a;

    private Singleton() {
        a = 1;
    }

    public static Singleton getSingleton() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    Singleton obj = new Singleton();
                    // 2 写屏障保证局部变量obj的写入顺序与全局变量的写入有序性
                    UNSAFE.storeFence();
                    Singleton.singleton = obj;
                }
            }
        }
        return singleton;
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            System.out.println(getSingleton());
        }
        long end = System.nanoTime();
        System.out.println(end - start);
    }
}
