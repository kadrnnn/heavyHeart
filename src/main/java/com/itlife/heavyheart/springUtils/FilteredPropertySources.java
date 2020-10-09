package com.itlife.heavyheart.springUtils;

import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.StreamSupport;

/**
 * @Author kex
 * @Create 2020/7/22 16:01
 * @Description
 */
public class FilteredPropertySources implements PropertySources {
    private final PropertySources delegate;
    private final Set<String> filtered;

    FilteredPropertySources(PropertySources delegate, String... filtered) {
        this.delegate = delegate;
        this.filtered = new HashSet(Arrays.asList(filtered));
    }

    public boolean contains(String name) {
        return this.included(name) ? this.delegate.contains(name) : false;
    }

    public PropertySource<?> get(String name) {
        return this.included(name) ? this.delegate.get(name) : null;
    }

    public Iterator<PropertySource<?>> iterator() {
        return StreamSupport.stream(this.delegate.spliterator(), false).filter(this::included).iterator();
    }

    private boolean included(PropertySource<?> propertySource) {
        return this.included(propertySource.getName());
    }

    private boolean included(String name) {
        return !this.filtered.contains(name);
    }
}
