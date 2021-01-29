package com.itlife.heavyheart.test;

/**
 * @Author kex
 * @Create 2021/1/18 16:18
 * @Description
 */
public class Son extends Base {
    private int i = 1;

    @Override
    void print() {
        System.out.println("i="+ i );
    }

    int get(int x){
        int a =Integer.SIZE - 3;
        int b = (1 << a ) -1;
        //return x & ~b;
        return x & b;
    }

    public static void main(String[] args){
        Son son = new Son();
        System.out.println((1 << 29 ) -1);
        System.out.println(son.get(10));
    }
}
