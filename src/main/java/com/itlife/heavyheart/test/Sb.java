package com.itlife.heavyheart.test;

/**
 * @Author kex
 * @Create 2021/1/28 17:17
 * @Description
 */
class Sb {
    protected class Nc {
        public Nc() {
            System.out.println("1");
        }

        public void f() {
            System.out.println("2");
        }
    }

    private Nc n = new Nc();

    Sb() {
        System.out.println("3");
    }

    public void insertNc(Nc nc) {
        n = nc;
    }

    public void g() {
        n.f();
    }
}