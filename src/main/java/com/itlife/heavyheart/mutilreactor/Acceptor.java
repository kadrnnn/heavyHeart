package com.itlife.heavyheart.mutilreactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Acceptor implements Runnable{

    final Selector sel;
    final ServerSocketChannel serverSocket;
    int handleNext = 0;

    private final int POOL_SIZE=Runtime.getRuntime().availableProcessors();
    private Executor subReactorExecutor= Executors.newFixedThreadPool(POOL_SIZE);

    private Reactor[] subReactors=new Reactor[POOL_SIZE-1];

    public Acceptor(Selector sel, int port) throws IOException {
        this.sel = sel;
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port)); // 绑定端口
        // 设置成非阻塞模式
        serverSocket.configureBlocking(false);
        // 注册到 选择器 并设置处理 socket 连接事件
        serverSocket.register(sel, SelectionKey.OP_ACCEPT,this);
        init();
        System.out.println("mainReactor-" + "Acceptor: Listening on port: " + port);
    }
    public void init() throws IOException {
        for (int i = 0; i < subReactors.length; i++) {
            subReactors[i]=new Reactor();
            subReactorExecutor.execute(subReactors[i]);
        }
    }
    @Override
    public synchronized void run() {
        try {
            // 接收连接，非阻塞模式下，没有连接直接返回 null
            SocketChannel sc = serverSocket.accept();
            if (sc != null) {
                // 把提示发到界面
                sc.write(ByteBuffer.wrap("Multiply Reactor Pattern Example\r\nreactor> ".getBytes()));
                System.out.println(Thread.currentThread().getName()+":Main-Reactor-Acceptor: " + sc.socket().getLocalSocketAddress() +" 注册到 subReactor-" + handleNext);
                // 如何解决呢，直接调用 wakeup，有可能还没有注册成功又阻塞了。这是一个多线程同步的问题，可以借助队列进行处理
                Reactor subReactor = subReactors[handleNext];
                subReactor.register(new AsyncHandler(sc));
                if(++handleNext == subReactors.length) {
                    handleNext = 0;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
