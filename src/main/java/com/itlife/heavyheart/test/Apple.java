package com.itlife.heavyheart.test;

import lombok.Data;

/**
 * @Author kex
 * @Create 2021/1/4 10:26
 * @Description
 */
public class Apple {
    private Double weight;
    private Double price;

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Apple(Double weight) {
        this.weight = weight;
    }
}
