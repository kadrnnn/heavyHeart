package com.itlife.heavyheart.dto;

import java.math.BigDecimal;

/**
 * @Author kex
 * @Create 2020/8/17 15:05
 * @Description
 */
public class Test {
    private Integer id;
    private String name;
    private BigDecimal value;
    private Integer num;

    public Test(Integer id, String name, BigDecimal value, Integer num) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.num = num;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
