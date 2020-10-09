package com.itlife.heavyheart.nettygo.client;

/**
 * @Author kex
 * @Create 2020/9/9 15:22
 * @Description
 */
public class TestClient {
    public static void main(String[] args) {

        //开启10条线程，每条线程就相当于一个客户端
        for (int i = 1; i <= 10; i++) {

            new Thread(new NettyClient("thread" + "--" + i)).start();
        }
    }
}
