package com.itlife.heavyheart.springUtils;

import com.itlife.heavyheart.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.PlaceholdersResolver;
import org.springframework.boot.context.properties.bind.handler.IgnoreTopLevelConverterNotFoundBindHandler;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySources;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @Author kex
 * @Create 2020/7/22 15:26
 * @Description
 */
@Component
@Order(-2147483648)
public class SpringContextUtils implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(SpringContextUtils.class);
    private static ApplicationContext APPLICATION_CONTEXT;
    private static DefaultListableBeanFactory BEAN_DEFINITION_REGISTRY;
    private static Binder binder;

    public SpringContextUtils() {
    }

    public static <T> T getBean(String name) throws BeansException {
        return (T) APPLICATION_CONTEXT.getBean(name);
    }

    public static <T> T getBean(Class<T> clz) throws BeansException {
        return APPLICATION_CONTEXT.getBean(clz);
    }

    public static <T> T getBean(String beanName, Class<T> clz) throws BeansException {
        return APPLICATION_CONTEXT.getBean(beanName, clz);
    }

    public static WebServer getWebServer() {
        return APPLICATION_CONTEXT instanceof WebServerApplicationContext ? ((WebServerApplicationContext)APPLICATION_CONTEXT).getWebServer() : null;
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> clz) throws BeansException {
        return APPLICATION_CONTEXT.getBeansOfType(clz);
    }

    public static String[] getBeanNamesForType(Class<?> clz) throws BeansException {
        return APPLICATION_CONTEXT.getBeanNamesForType(clz);
    }

    public static boolean containsBean(String name) {
        return APPLICATION_CONTEXT.containsBean(name);
    }

    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return APPLICATION_CONTEXT.isSingleton(name);
    }

    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return APPLICATION_CONTEXT.getType(name);
    }

    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return APPLICATION_CONTEXT.getAliases(name);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext)applicationContext;
        BEAN_DEFINITION_REGISTRY = (DefaultListableBeanFactory)configurableApplicationContext.getBeanFactory();
        PropertySources propertySource = (new PropertySourcesDeducer(APPLICATION_CONTEXT)).getPropertySources();
        binder = new Binder(ConfigurationPropertySources.from(propertySource), (PlaceholdersResolver)null, (ConversionService)null, (Consumer)null);
    }

    public static void registerSingleton(String beanName, Object singletonObject) {
        registerSingleton(beanName, singletonObject, (String)null);
    }

    public static void registerSingleton(String beanName, Object singletonObject, String prefix) {
        BEAN_DEFINITION_REGISTRY.registerSingleton(beanName, singletonObject);
        loadProperties(singletonObject, prefix);
    }

    public static void registerBean(Class<?> clz, boolean isPrimary) {
        registerBean(clz, (String)null, isPrimary);
    }

    public static void registerBean(Class<?> clz, String prefix) {
        registerBean(clz, prefix, false);
    }

    public static void registerBean(Class<?> clz) {
        registerBean(clz, (String)null, false);
    }

    public static void registerBean(Class<?> clz, String prefix, boolean isPrimary) {
        BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(clz).getBeanDefinition();
        beanDefinition.setPrimary(isPrimary);
        String beanName = Introspector.decapitalize(ClassUtils.getShortName(beanDefinition.getBeanClassName()));
        BEAN_DEFINITION_REGISTRY.registerBeanDefinition(beanName, beanDefinition);
        loadProperties(APPLICATION_CONTEXT.getBean(beanName), prefix);
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        return BEAN_DEFINITION_REGISTRY.getBeansWithAnnotation(annotationType);
    }

    public static void loadProperties(Object singletonObject, String prefix) {
        if (StringUtils.hasLength(prefix)) {
            ResolvableType type = ResolvableType.forClass(singletonObject.getClass());
            Bindable<?> target = Bindable.of(type).withExistingValue(singletonObject);
            binder.bind(prefix, target, new IgnoreTopLevelConverterNotFoundBindHandler());
        }

    }

    public static Environment getEnvironment() {
        return APPLICATION_CONTEXT.getEnvironment();
    }

    public static void registerAlias(String beanName, String aliasName) {
        log.info("Alias definition '" + aliasName + "' registered for name '" + beanName + "'");
        BEAN_DEFINITION_REGISTRY.registerAlias(beanName, aliasName);
    }

    public static void registerAliasByExistsBean(Object beanObject, String aliasName) {
        Assert.notNull(beanObject, "'beanObject' must not be empty");
        Assert.hasText(aliasName, "'alias' must not be empty");
        Map<String, ?> beanNameToBeanObjectMap = getBeansOfType(beanObject.getClass());
        String beanName = null;
        if (beanNameToBeanObjectMap.containsKey(aliasName)) {
            log.warn("register aliasName exists{}.", aliasName);
        } else {
            if (!beanNameToBeanObjectMap.isEmpty()) {
                Iterator var4 = beanNameToBeanObjectMap.entrySet().iterator();

                while(var4.hasNext()) {
                    Map.Entry<String, ?> entry = (Map.Entry)var4.next();
                    if (entry.getValue() == beanObject) {
                        beanName = (String)entry.getKey();
                        break;
                    }
                }
            }

            if (StringUtils.isEmpty(beanName)) {
                throw new BeanInitializationException("给" + beanObject + "注册别名" + aliasName + ",未找到对应加载的类.");
            } else {
                registerAlias(beanName, aliasName);
            }
        }
    }

    public static void unregisterBean(String beanName) {
        try {
            BEAN_DEFINITION_REGISTRY.removeBeanDefinition(beanName);
        } catch (NoSuchBeanDefinitionException var2) {
            BEAN_DEFINITION_REGISTRY.destroySingleton(beanName);
        }

    }

    public static void unregisterAlias(String aliasName) {
        BEAN_DEFINITION_REGISTRY.removeAlias(aliasName);
    }

    public static ListableBeanFactory getBeanFactory() {
        return BEAN_DEFINITION_REGISTRY;
    }

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }
}
