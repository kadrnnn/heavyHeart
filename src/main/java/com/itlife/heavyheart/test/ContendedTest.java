package com.itlife.heavyheart.test;

/**
 * @Author kex
 * @Create 2020/11/2 17:37
 * @Description
 */

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * VM Options:
 * -javaagent:/Users/sangjian/dev/source-files/classmexer-0_03/classmexer.jar
 * -XX:-RestrictContended
 */
public class ContendedTest {

    byte a;
    @sun.misc.Contended("a")
    long b;
    @sun.misc.Contended("a")
    long c;
    int d;

    private static Unsafe UNSAFE;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws NoSuchFieldException {
        System.out.println("offset-a: " + UNSAFE.objectFieldOffset(ContendedTest.class.getDeclaredField("a")));
        System.out.println("offset-b: " + UNSAFE.objectFieldOffset(ContendedTest.class.getDeclaredField("b")));
        System.out.println("offset-c: " + UNSAFE.objectFieldOffset(ContendedTest.class.getDeclaredField("c")));
        System.out.println("offset-d: " + UNSAFE.objectFieldOffset(ContendedTest.class.getDeclaredField("d")));

        ContendedTest contendedTest = new ContendedTest();

        // 打印对象的shallow size
        //System.out.println("Shallow Size: " + MemoryUtil.memoryUsageOf(contendedTest) + " bytes");
        // 打印对象的 retained size
        //System.out.println("Retained Size: " + MemoryUtil.deepMemoryUsageOf(contendedTest) + " bytes");
    }

}
