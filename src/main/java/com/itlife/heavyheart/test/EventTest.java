package com.itlife.heavyheart.test;

import com.itlife.heavyheart.eventprocessing.AbstractKadrnListener;
import com.itlife.heavyheart.eventprocessing.KadrnEventPublisher;
import org.aspectj.weaver.ast.Call;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * @Author kex
 * @Create 2020/7/28 16:54
 * @Description
 */
public class EventTest {
    public static void main(String[] args) throws InterruptedException {
        KadrnEventPublisher kadrnEventPublisher = new KadrnEventPublisher();
        kadrnEventPublisher.addEventListener(new AbstractKadrnListener());
        kadrnEventPublisher.methodMonitor();
    }
}
