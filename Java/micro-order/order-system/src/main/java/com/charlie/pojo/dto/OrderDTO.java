package com.charlie.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 订单列表
     */
    private List<Order> orders;

    @Data
    public static class Order{
        /**
         * 商品id
         */
        private String itemId;
        /**
         * 商品数量
         */
        private int count;
    }
}
