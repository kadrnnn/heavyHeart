package com.itlife.heavyheart.springUtils;

import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * @Author kex
 * @Create 2020/7/22 16:02
 * @Description
 */
public final class CompositePropertySources implements PropertySources {
    private final List<PropertySources> propertySources;

    CompositePropertySources(PropertySources... propertySources) {
        this.propertySources = Arrays.asList(propertySources);
    }

    public Iterator<PropertySource<?>> iterator() {
        return this.propertySources.stream().flatMap((sources) -> {
            return StreamSupport.stream(sources.spliterator(), false);
        }).iterator();
    }

    public boolean contains(String name) {
        Iterator var2 = this.propertySources.iterator();

        PropertySources sources;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            sources = (PropertySources)var2.next();
        } while(!sources.contains(name));

        return true;
    }

    public PropertySource<?> get(String name) {
        Iterator var2 = this.propertySources.iterator();

        PropertySource source;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            PropertySources sources = (PropertySources)var2.next();
            source = sources.get(name);
        } while(source == null);

        return source;
    }
}
