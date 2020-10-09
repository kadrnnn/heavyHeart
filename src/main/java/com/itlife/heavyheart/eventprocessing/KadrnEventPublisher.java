package com.itlife.heavyheart.eventprocessing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author kex
 * @Create 2020/7/28 16:41
 * @Description
 */
public class KadrnEventPublisher {
    private List<KadrnListener> listeners = new ArrayList<>();

    public void methodMonitor() throws InterruptedException {
        KadrnEvent event = new KadrnEvent(this);
        publishEvent("begin", event);
        TimeUnit.SECONDS.sleep(5);
        publishEvent("end", event);
    }

    private void publishEvent(String status, KadrnEvent event) {
        List<KadrnListener> copy = new ArrayList<>(listeners);
        copy.forEach(listener -> {
            if ("begin".equals(status)) {
                listener.onMethodBegin(event);
            } else {
                listener.onMethodEnd(event);
            }
        });
    }

    // 省略实现
    public void addEventListener(KadrnListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(KadrnListener listener) {
    }

    public void removeAllListeners() {
    }
}
