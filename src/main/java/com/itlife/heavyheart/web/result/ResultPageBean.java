package com.itlife.heavyheart.web.result;

import org.springframework.data.domain.Page;

/**
 * @Author kex
 * @Create 2020/8/14 9:50
 * @Description
 */
public class ResultPageBean<D> extends ResultBean<D> {
    private PageBean page;

    public ResultPageBean() {
    }

    public ResultPageBean(String code, String message) {
        super(code, message);
    }

    public ResultPageBean(String code, String message, D data) {
        super(code, message, data);
    }

    public ResultPageBean(String code, String message, D data, PageBean page) {
        super(code, message, data);
        this.page = page;
    }

    public ResultPageBean<D> bindPage(PageBean pageBean) {
        this.page = pageBean;
        return this;
    }

    public ResultPageBean<D> bindPage(Page<D> page) {
        if (page != null) {
            this.setData((D) page.getContent());
            this.bindPage(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalPages());
        }

        return this;
    }

    public ResultPageBean<D> bindPage(Integer pageNumber, Integer pageSize, Long totalSize, Integer totalPageCount) {
        this.page = new PageBean(pageNumber, pageSize, totalSize, totalPageCount);
        return this;
    }

    public PageBean getPage() {
        return this.page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }
}
