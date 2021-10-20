package com.itlife.heavyheart.multireactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Reactor implements Runnable{
    private ConcurrentLinkedQueue<AsyncHandler> events=new ConcurrentLinkedQueue<>();
    private final Selector selector;

    public Reactor() throws IOException {
        this.selector = Selector.open();
    }

    public Selector getSelector(){
        return selector;
    }
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                AsyncHandler handler;
                while ((handler = events.poll()) != null) {
                    handler.getChannel().configureBlocking(false);
                    SelectionKey sk=handler.getChannel().register(selector, SelectionKey.OP_READ);
                    sk.attach(handler);
                    handler.setSk(sk);
                }
                selector.select(); //阻塞
                Set<SelectionKey> selectionKeys=selector.selectedKeys();
                Iterator<SelectionKey> it=selectionKeys.iterator();
                while(it.hasNext()){
                    SelectionKey key=it.next();
                    //获取attach方法传入的附加对象
                    Runnable runnable=(Runnable)key.attachment();
                    if(runnable!=null){
                        runnable.run();
                    }
                    it.remove();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void register(AsyncHandler asyncHandler){
        events.offer(asyncHandler);
        selector.wakeup();
    }
}