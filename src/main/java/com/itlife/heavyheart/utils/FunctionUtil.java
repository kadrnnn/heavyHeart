package com.itlife.heavyheart.utils;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @Author kex
 * @Create 2020/8/18 18:00
 * @Description
 */
public class FunctionUtil {
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 业务数据JSON转换对象
     *
     * @param businessDate 业务数据
     * @param key          map中的键
     * @param type         实体类型
     * @param dateFormat   日期类型
     * @return T
     */
    public static <T> T convertData(Map businessDate, String key, Class type, DateFormat dateFormat) {
        Object data = businessDate.get(key);
        String dataJson = JsonUtil.writeValueAsString(data, dateFormat);
        if (StringUtils.isEmpty(dataJson) || "\"\"".equals(dataJson) || "null".equals(dataJson) || "undefined".equals(dataJson)) {
            return null;
        }
        return JsonUtil.readValue(dataJson, type, dateFormat);
    }
}
