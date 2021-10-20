package com.itlife.heavyheart.multireactor;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MultiplyReactor {
    public static void main(String[] args) throws IOException {
        MultiplyReactor mr = new MultiplyReactor(8888);
        mr.start();
    }
    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
    // Reactor（Selector） 线程池，其中一个线程被 mainReactor 使用，剩余线程都被 subReactor 使用
    static Executor mainReactorExecutor = Executors.newFixedThreadPool(POOL_SIZE);
    // 主 Reactor，接收连接，把 SocketChannel 注册到从 Reactor 上
    private Reactor mainReactor;
    private int port;

    public MultiplyReactor(int port) {
        try {
            this.port = port;
            mainReactor = new Reactor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 启动主从 Reactor，初始化并注册 Acceptor 到主 Reactor
     */
    public void start() throws IOException {
        new Acceptor(mainReactor.getSelector(), port); // 将 ServerSocketChannel 注册到 mainReactor
        mainReactorExecutor.execute(mainReactor); //使用线程池来处理main Reactor的连接请求
    }
}