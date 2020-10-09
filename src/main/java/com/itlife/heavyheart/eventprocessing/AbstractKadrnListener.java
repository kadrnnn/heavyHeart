package com.itlife.heavyheart.eventprocessing;

/**
 * @Author kex
 * @Create 2020/7/28 16:24
 * @Description
 */
public class AbstractKadrnListener implements KadrnListener {
    @Override
    public void onMethodBegin(KadrnEvent event) {
        event.timestamp = System.currentTimeMillis();
    }

    @Override
    public void onMethodEnd(KadrnEvent event) {
        Long duration = System.currentTimeMillis() - event.timestamp;
        System.out.println("耗时：" + duration );
    }
}
