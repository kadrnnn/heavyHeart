package com.itlife.heavyheart;

import com.itlife.heavyheart.nettygo.server.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.net.InetSocketAddress;

@SpringBootApplication
@EnableEurekaServer
@EnableFeignClients
@EnableCaching
public class HeavyheartApplication implements CommandLineRunner {
    @Autowired
    NettyServer nettyServer;

    private static final Logger log = LoggerFactory.getLogger(HeavyheartApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HeavyheartApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", Integer.parseInt("8888"));
        log.info("netty服务器启动地址：" + "127.0.0.1");
        nettyServer.start(address);
    }
}
