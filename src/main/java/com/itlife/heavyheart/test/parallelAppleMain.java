package com.itlife.heavyheart.test;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author kex
 * @Create 2021/1/4 10:30
 * @Description 并行流
 */
@Slf4j
public class parallelAppleMain {
    /**
     * 并行流的使用注意
     *
     * 在并行流的使用上有下面几点需要注意：
     * 尽量使用 LongStream / IntStream / DoubleStream 等原始数据流代替 Stream 来处理数字，以避免频繁拆装箱带来的额外开销
     * 要考虑流的操作流水线的总计算成本，假设 N 是要操作的任务总数，Q 是每次操作的时间。N * Q 就是操作的总时间，Q 值越大就意味着使用并行流带来收益的可能性越大
     * 例如：前端传来几种类型的资源，需要存储到数据库。每种资源对应不同的表。我们可以视作类型数为 N，存储数据库的网络耗时 + 插入操作耗时为 Q。一般情况下网络耗时都是比较大的。因此该操作就比较适合并行处理。当然当类型数目大于核心数时，该操作的性能提升就会打一定的折扣了。
     * 对于较少的数据量，不建议使用并行流
     * 容易拆分成块的流数据，建议使用并行流
     *
     *以下是一些常见的集合框架对应流的可拆分性能表
     * ArrayList	极佳
     * LinkedList	差
     * IntStream.range	极佳
     * Stream.iterate	差
     * HashSet	好
     * TreeSet	好
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        List<Apple> apples = new ArrayList<>();
        apples.add(new Apple(2.0));
        apples.add(new Apple(3.0));
        apples.add(new Apple(4.0));
        apples.add(new Apple(5.0));
        Date begin = new Date();
//        for (Apple apple : apples) {
//            apple.setPrice(5.0 * apple.getWeight() / 1000);
//            Thread.sleep(1000);
//        }


        /*apples.parallelStream().forEach(apple -> {
            apple.setPrice(5.0 * apple.getWeight() / 1000);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });*/
        Date end = new Date();
        log.info("苹果数量：{}个, 耗时：{}s", apples.size(), (end.getTime() - begin.getTime()) / 1000);
    }
}
