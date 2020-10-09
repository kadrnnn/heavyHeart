package com.itlife.heavyheart.web.mvc.method.argumentresolver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author kex
 * @Create 2020/8/14 10:27
 * @Description
 */
public class MapWapper<K, V> {
    private Map<K, V> innerMap = new HashMap();

    public MapWapper() {
    }

    public void setInnerMap(Map<K, V> innerMap) {
        this.innerMap = innerMap;
    }

    public Map<K, V> getInnerMap() {
        return this.innerMap;
    }

    public void clear() {
        this.innerMap.clear();
    }

    public boolean containsKey(Object key) {
        return this.innerMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.innerMap.containsValue(value);
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return this.innerMap.entrySet();
    }

    public boolean equals(Object o) {
        return this.innerMap.equals(o);
    }

    public V get(Object key) {
        return this.innerMap.get(key);
    }

    public int hashCode() {
        return this.innerMap.hashCode();
    }

    public boolean isEmpty() {
        return this.innerMap.isEmpty();
    }

    public Set<K> keySet() {
        return this.innerMap.keySet();
    }

    public V put(K key, V value) {
        return this.innerMap.put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        this.innerMap.putAll(m);
    }

    public V remove(Object key) {
        return this.innerMap.remove(key);
    }

    public int size() {
        return this.innerMap.size();
    }

    public Collection<V> values() {
        return this.innerMap.values();
    }

    public Map<K, V> toMap() {
        return this.innerMap;
    }

    public String toString() {
        return this.innerMap.toString();
    }
}
