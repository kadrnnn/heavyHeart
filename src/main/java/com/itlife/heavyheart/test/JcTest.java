package com.itlife.heavyheart.test;

/**
 * @Author kex
 * @Create 2020/5/26 14:06
 * @Description
 */
public class JcTest {
    public static void main(String[] args) {
        JcTest jcTest = new JcTest();
        int n = 10;
        System.out.println(jcTest.good(n));
    }

    private int good(int n) {
        if (n == 1) {
            return 1;
        } else {
            return n * good(n - 1);
        }
    }
}
