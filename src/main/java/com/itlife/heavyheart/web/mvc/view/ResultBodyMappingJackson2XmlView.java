package com.itlife.heavyheart.web.mvc.view;

import com.itlife.heavyheart.web.result.ResultBean;
import com.itlife.heavyheart.web.result.ResultBody;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.view.xml.MappingJackson2XmlView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author kex
 * @Create 2020/8/14 10:05
 * @Description
 */
public class ResultBodyMappingJackson2XmlView extends MappingJackson2XmlView {
    @Resource
    MessageSource messageSource;

    public ResultBodyMappingJackson2XmlView() {
    }

    protected Object filterAndWrapModel(Map<String, Object> modelMap, HttpServletRequest request) {
        Object retModelValue = modelMap;
        if (modelMap != null && modelMap instanceof Map && ((Map) modelMap).size() > 0) {
            Map<String, Object> mapValue = (Map) modelMap;
            Iterator var5 = mapValue.values().iterator();

            while (var5.hasNext()) {
                Object mapV = var5.next();
                if (mapV instanceof ResultBody) {
                    if (mapV instanceof ResultBean) {
                        ResultBean resultBean = (ResultBean) mapV;

                        try {
                            resultBean.setMessage(this.messageSource.getMessage(resultBean.getCode(), (Object[]) null, LocaleContextHolder.getLocale()));
                        } catch (Exception var9) {
                        }
                    }

                    retModelValue = mapV;
                    break;
                }
            }
        }

        return retModelValue;
    }
}
