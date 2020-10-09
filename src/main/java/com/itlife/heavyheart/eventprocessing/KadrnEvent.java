package com.itlife.heavyheart.eventprocessing;

import org.springframework.context.ApplicationEvent;

import java.util.EventObject;

/**
 * @Author kex
 * @Create 2020/7/28 16:02
 * @Description
 */
public class KadrnEvent extends EventObject {
    public long timestamp;

    public KadrnEvent(Object source) {
        super(source);
    }
}
