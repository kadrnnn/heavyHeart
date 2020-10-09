package com.itlife.heavyheart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author kex
 * @Create 2020/7/22 10:03
 * @Description
 */
@Component
public class ThreadPool {
    private static final Logger log = LoggerFactory.getLogger(ThreadPool.class);

    /**
     * 线程池配置
     */
    public static final int POOL_HXXC = 6; //核心线程数
    public static final int POOL_MAXXC = 12; //最大线程数
    public static final long POOL_XCSJ = 2; //线程保持时间
    public static final int POOL_DLDX = 18; //等待队列大小
    public static final int MAX_COUNT = 5000;
    public static final ThreadPoolExecutor.CallerRunsPolicy POOL_JJCL = new ThreadPoolExecutor.CallerRunsPolicy(); //拒绝策略，改为由当前线程执行任务
    public static final int M_CSSJ = 60; // 60秒没结果返回，门就自动通过
    ExecutorService executorService = null;

    {
        executorService = Executors.newFixedThreadPool(6);
    }

    /**
     * 线程池
     */
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(ThreadPool.POOL_HXXC, ThreadPool.POOL_MAXXC, ThreadPool.POOL_XCSJ
            , TimeUnit.MINUTES, new LinkedBlockingDeque<>(ThreadPool.POOL_DLDX), ThreadPool.POOL_JJCL);

    public String processing() {
        Date start = new Date();
        CountDownLatch countDownLatch = new CountDownLatch(6);
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    list.add("a");
                } catch (Exception e) {
                    log.error("错误", e);
                } finally {
                    countDownLatch.countDown();
                }
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("错误", e);
        }
        Date end = new Date();
        log.info("耗时：" + (end.getTime() - start.getTime()) + "毫秒");
        return null;
    }
}
