package com.itlife.heavyheart.utils;

import com.itlife.heavyheart.web.mvc.method.argumentresolver.MapWapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ： kexi
 * @date ：Created in 2019/10/14 11:02
 * @description：参数检验
 * @modified By：
 * @version: 1.0.0$
 */

@Component
public class ParamUtils {

    private static ParamUtils SINGLETON;

    /**
     * 单例
     *
     * @return
     */
    public static ParamUtils instance() {
        if (SINGLETON == null) {
            SINGLETON = new ParamUtils();
        }
        return SINGLETON;
    }

    /**
     * 初始化
     */
    @PostConstruct
    private void init() {
        SINGLETON = this;
    }


    /**
     * 删除空参数
     *
     * @param data 参数
     * @return Map<String, Object>
     * @author: kexi
     * @date: 2019/5/31
     */
    public Map<String, Object> removeEmptyData(MapWapper<String, Object> data) {
        Map<String, Object> map = data.getInnerMap();
        //使用迭代器的remove(Json)方法删除value值为空或者为空字符串的元素
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            if (entry.getValue() == null || StringUtils.isBlank(entry.getValue().toString())) {
                it.remove();
            }
        }
        return map;
    }

}
