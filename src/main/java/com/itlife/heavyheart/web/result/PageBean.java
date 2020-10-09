package com.itlife.heavyheart.web.result;

import java.io.Serializable;

/**
 * @Author kex
 * @Create 2020/8/14 9:41
 * @Description
 */
public class PageBean implements Serializable {
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalSize;
    private Integer totalPageCount;

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public Long getTotalSize() {
        return this.totalSize;
    }

    public Integer getTotalPageCount() {
        return this.totalPageCount;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public void setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public PageBean() {
    }

    public PageBean(Integer pageNumber, Integer pageSize, Long totalSize, Integer totalPageCount) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.totalPageCount = totalPageCount;
    }
}
