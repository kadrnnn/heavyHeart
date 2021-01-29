package com.itlife.heavyheart.test;

import com.itlife.heavyheart.project.model.Address;
import com.itlife.heavyheart.project.model.BaseEntity;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 * @Author kex
 * @Create 2020/5/26 14:06
 * @Description
 */
public class JcTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        JcTest jcTest = new JcTest();
        int n = 10;
        System.out.println(jcTest.good(n));
        jcTest.xor(3, 4);
        //StudentBean studentBean = StudentBean.builder().age(22).name("q").build();
        CompletableFuture future = new CompletableFuture();
        future.complete("完成");
        System.out.println(future.get().toString());
    }

    private int good(int n) {
        if (n == 1) {
            return 1;
        } else {
            return n * good(n - 1);
        }
    }

    //异或xor  交换两个数的值    异或：自己与自己异或为零， 自己与零异或为自己
    // True ⊕ False = True
    // False ⊕ True = True
    // False⊕ False = False
    // True ⊕ True = False
    private void xor(int a, int b) {
        a = a ^ b;
        b = b ^ a;
        a = a ^ b;
        System.out.println("a:{" + a + "},b:{" + b + "}");
    }
}
