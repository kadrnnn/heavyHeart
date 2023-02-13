package com.itlife.heavyheart.test;

import lombok.SneakyThrows;

public class BatchConsumeQueueTest {
    @SneakyThrows
    public static void main(String[] args) {
        BatchConsumeQueue<String> batchConsumeQueue = new BatchConsumeQueue<>(100000, 100, 10, 1000, list -> {
            System.out.println(list.size());
            for (String s : list) {
                System.out.println(s);
            }
        });
        for (int i = 0; i < 10000; i++) {
            batchConsumeQueue.add("我是:" +Thread.currentThread().getName()+ i);
        }
    }
}
