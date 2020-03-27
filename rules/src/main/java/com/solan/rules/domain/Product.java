package com.solan.rules.domain;

import lombok.Data;

/**
 *
 * @author: hyl
 * @date: 2020/3/27 14:55
 */
@Data
public class Product {
    /**
     * 价格
     */
    private double price;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 备注说明
     */
    private String message;
}
