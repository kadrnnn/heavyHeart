package com.itlife.heavyheart.nettygo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @Author kex
 * @Create 2020/9/9 14:25
 * @Description netty服务启动类
 */
@Component
public class NettyServer {
    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    public void start(InetSocketAddress address) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);
        try {
            // 3种模型 主从模型，单线程模型，混杂模型
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup) // 绑定线程池
                    .channel(NioServerSocketChannel.class)
                    .localAddress(address)
                    .childHandler(new NettyServerChannelInitializer()) // 编码解码 解包粘包
                    .option(ChannelOption.SO_BACKLOG, 128) // 服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // 保持长连接，2小时无数据激活心跳机制

            // 绑定端口，开始接收进来的连接
            ChannelFuture future = bootstrap.bind(address).sync();
            log.info("netty服务器开始监听端口：" + address.getPort());
            // 关闭channel和块，直到它被关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
