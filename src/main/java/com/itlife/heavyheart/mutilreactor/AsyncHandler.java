package com.itlife.heavyheart.mutilreactor;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class AsyncHandler implements Runnable{
    private SocketChannel channel;
    private SelectionKey sk;

    ByteBuffer inputBuffer=ByteBuffer.allocate(1024);
    ByteBuffer outputBuffer=ByteBuffer.allocate(1024);
    StringBuilder builder=new StringBuilder(); //存储客户端的完整消息

    public AsyncHandler(SocketChannel channel){
        this.channel=channel;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void setSk(SelectionKey sk) {
        this.sk = sk;
    }

    @Override
    public void run() {
        try {
            if (sk.isReadable()) {
                read();
            } else if (sk.isWritable()) {
                write();
            }
        }catch (Exception e){
            try {
                this.sk.channel().close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    protected void read() throws IOException {
        inputBuffer.clear();
        int n=channel.read(inputBuffer);
        if(inputBufferComplete(n)){
            System.out.println(Thread.currentThread().getName()+":Server端收到客户端的请求消息："+builder.toString());
            outputBuffer.put(builder.toString().getBytes(StandardCharsets.UTF_8));
            this.sk.interestOps(SelectionKey.OP_WRITE); //更改服务的逻辑状态以及处理的事件类型
        }
    }

    private boolean inputBufferComplete(int bytes) throws EOFException {
        if(bytes>0){
            inputBuffer.flip(); //转化成读取模式
            while(inputBuffer.hasRemaining()){ //判断缓冲区中是否还有元素
                byte ch=inputBuffer.get(); //得到输入的字符
                if(ch==3){ //表示Ctrl+c 关闭连接
                    throw new EOFException();
                }else if(ch=='\r'||ch=='\n'){ //表示换行符
                    return true;
                }else{
                    builder.append((char)ch); //拼接读取到的数据
                }
            }
        }else if(bytes==-1){
            throw new EOFException(); //客户端关闭了连接
        }
        return false;
    }

    private void write() throws IOException {
        int written=-1;
        outputBuffer.flip(); //转化为读模式，判断是否有数据需要发送
        if(outputBuffer.hasRemaining()){
            written=channel.write(outputBuffer); //把数据写回客户端
        }
        outputBuffer.clear();
        builder.delete(0,builder.length());
        if(written<=0){ //表示客户端没有输信息
            this.sk.channel().close();
        }else{
            channel.write(ByteBuffer.wrap("\r\nreactor>".getBytes()));
            this.sk.interestOps(SelectionKey.OP_READ);
        }
    }
}
