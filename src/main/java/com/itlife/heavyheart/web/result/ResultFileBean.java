package com.itlife.heavyheart.web.result;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author kex
 * @Create 2020/8/14 9:47
 * @Description
 */
public class ResultFileBean implements ResultBody, Serializable {
    private Map<String, Object> files = new HashMap();
    private String contentType = "application/octet-stream";
    private String fileName;

    public ResultFileBean(String fileName) {
        this.fileName = fileName;
    }

    public ResultFileBean contentType(String contentType) {
        if (StringUtils.isNotEmpty(contentType)) {
            this.contentType = contentType;
        }

        return this;
    }

    public ResultFileBean fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public ResultFileBean addFile(String fileName, Object file) {
        this.files.put(fileName, file);
        return this;
    }

    public ResultFileBean addFiles(Map<String, Object> addFiles) {
        this.files.putAll(addFiles);
        return this;
    }

    public ResultFileBean initFiles(Map<String, Object> initFiles) {
        this.files = initFiles;
        return this;
    }

    public Map<String, Object> getFiles() {
        return this.files;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFiles(Map<String, Object> files) {
        this.files = files;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ResultFileBean(Map<String, Object> files, String contentType, String fileName) {
        this.files = files;
        this.contentType = contentType;
        this.fileName = fileName;
    }

    public ResultFileBean() {
    }
}
