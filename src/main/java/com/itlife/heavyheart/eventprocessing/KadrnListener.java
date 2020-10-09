package com.itlife.heavyheart.eventprocessing;


import java.util.EventListener;

/**
 * @Author kex
 * @Create 2020/7/28 16:19
 * @Description
 */
public interface KadrnListener extends EventListener {
    public void onMethodBegin(KadrnEvent event);

    public void onMethodEnd(KadrnEvent event);
}
