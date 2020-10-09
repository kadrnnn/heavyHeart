package com.itlife.heavyheart.web.result;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author kex
 * @Create 2020/8/14 9:43
 * @Description
 */
@XStreamAlias("xml")
@JacksonXmlRootElement(
        localName = "xml"
)
public class ResultBean<D> implements ResultBody, Serializable {
    public static final long serialVersionUID = 6788287645617643751L;
    private String code;
    private String message;
    @XStreamAlias("data")
    private D data;
    private Map<String, Object> ext;

    public ResultBean() {
    }

    public ResultBean(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultBean(String code, String message, D data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultBean addExtProperty(String key, Object value) {
        if (this.ext == null) {
            this.ext = new HashMap();
        }

        this.ext.put(key, value);
        return this;
    }

    public ResultBean addExtProperty(Map<String, Object> addExtProperty) {
        if (this.ext == null) {
            this.ext = new HashMap();
        }

        this.ext.putAll(addExtProperty);
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public D getData() {
        return this.data;
    }

    public Map<String, Object> getExt() {
        return this.ext;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(D data) {
        this.data = data;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }
}
