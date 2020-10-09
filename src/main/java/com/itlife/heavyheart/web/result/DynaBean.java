package com.itlife.heavyheart.web.result;

import java.util.HashMap;

/**
 * @Author kex
 * @Create 2020/8/14 10:00
 * @Description
 */
public class DynaBean<T extends DynaBean> extends HashMap {
    private static final long serialVersionUID = 2L;
    private boolean changed = false;

    public DynaBean() {
    }

    public T setProperty(String property, Object value) {
        this.put(property, value);
        this.changed = true;
        return (T) this;
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void commitChange() {
        this.changed = false;
    }

    public <V> V getProperty(String property) {
        return (V) this.get(property);
    }

    public boolean hasProperty(String property) {
        return this.containsKey(property);
    }
}
