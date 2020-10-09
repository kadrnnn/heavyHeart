package com.itlife.heavyheart.web.mvc.view;

import com.itlife.heavyheart.web.result.ResultBean;
import com.itlife.heavyheart.web.result.ResultBody;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author kex
 * @Create 2020/8/14 10:04
 * @Description
 */
public class ResultBodyMappingJackson2JsonView extends MappingJackson2JsonView {
    @Resource
    MessageSource messageSource;

    public ResultBodyMappingJackson2JsonView() {
    }

    protected Object filterAndWrapModel(Map<String, Object> modelMap, HttpServletRequest request) {
        Object retModelValue = super.filterAndWrapModel(modelMap, request);
        if (retModelValue != null && retModelValue instanceof Map && ((Map)retModelValue).size() > 0) {
            Map<String, Object> mapValue = (Map)retModelValue;
            Iterator var5 = mapValue.values().iterator();

            while(var5.hasNext()) {
                Object mapV = var5.next();
                if (mapV instanceof ResultBody) {
                    if (mapV instanceof ResultBean) {
                        try {
                            ResultBean resultBean = (ResultBean)mapV;
                            resultBean.setMessage(this.messageSource.getMessage(resultBean.getCode(), (Object[])null, LocaleContextHolder.getLocale()));
                        } catch (Exception var8) {
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
