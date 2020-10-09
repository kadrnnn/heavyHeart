package com.itlife.heavyheart.springUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySources;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @Author kex
 * @Create 2020/7/22 15:56
 * @Description
 */
public class PropertySourcesDeducer {
    private static final Logger log = LoggerFactory.getLogger(PropertySourcesDeducer.class);
    private final ApplicationContext applicationContext;

    public PropertySourcesDeducer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public PropertySources getPropertySources() {
        MutablePropertySources environmentPropertySources = this.extractEnvironmentPropertySources();
        PropertySourcesPlaceholderConfigurer placeholderConfigurer = this.getSinglePropertySourcesPlaceholderConfigurer();
        if (placeholderConfigurer == null) {
            Assert.state(environmentPropertySources != null, "Unable to obtain PropertySources from PropertySourcesPlaceholderConfigurer or Environment");
            return environmentPropertySources;
        } else {
            PropertySources appliedPropertySources = placeholderConfigurer.getAppliedPropertySources();
            return environmentPropertySources == null ? appliedPropertySources : this.merge(environmentPropertySources, appliedPropertySources);
        }
    }

    private MutablePropertySources extractEnvironmentPropertySources() {
        Environment environment = this.applicationContext.getEnvironment();
        return environment instanceof ConfigurableEnvironment ? ((ConfigurableEnvironment)environment).getPropertySources() : null;
    }

    private PropertySourcesPlaceholderConfigurer getSinglePropertySourcesPlaceholderConfigurer() {
        Map<String, PropertySourcesPlaceholderConfigurer> beans = this.applicationContext.getBeansOfType(PropertySourcesPlaceholderConfigurer.class, false, false);
        if (beans.size() == 1) {
            return (PropertySourcesPlaceholderConfigurer)beans.values().iterator().next();
        } else {
            if (beans.size() > 1 && log.isWarnEnabled()) {
                log.warn("Multiple PropertySourcesPlaceholderConfigurer beans registered " + beans.keySet() + ", falling back to Environment");
            }

            return null;
        }
    }

    private PropertySources merge(PropertySources environmentPropertySources, PropertySources appliedPropertySources) {
        FilteredPropertySources filtered = new FilteredPropertySources(appliedPropertySources, new String[]{"environmentProperties"});
        return new CompositePropertySources(new PropertySources[]{filtered, environmentPropertySources});
    }
}
