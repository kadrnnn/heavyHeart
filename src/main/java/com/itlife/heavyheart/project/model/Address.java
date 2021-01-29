package com.itlife.heavyheart.project.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @Author kex
 * @Create 2020/9/17 15:04
 * @Description
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class Address extends BaseEntity {
    private static final long serialVersionUID = -499010884211394846L;

    /**
     * 地址名称
     */
    private String name;

    /**
     * 父ID
     */
    private Long pid;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 级别（省市区县）
     */
    private Integer level;

    /**
     * 区域编码
     */
    private String adCode;

    /**
     * 行政区边界坐标点
     */
    private String polyline;

    /**
     * 城市中心点
     */
    private String center;
}
