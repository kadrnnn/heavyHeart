package com.itlife.heavyheart.test;

/**
 * @Author kex
 * @Create 2021/1/28 17:24
 * @Description
 */
public class BigSb extends Sb {
    public class Nc extends Sb.Nc{
        public Nc(){
            System.out.println("4");
        }

        @Override
        public void f() {
            System.out.println("5");
        }
    }
    public BigSb(){
        insertNc(new Nc());
    }

    public static void main(String[] args){
        Sb sb = new BigSb();
        sb.g();
    }

}
