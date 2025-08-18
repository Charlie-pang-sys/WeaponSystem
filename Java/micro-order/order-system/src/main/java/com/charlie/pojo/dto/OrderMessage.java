package com.charlie.pojo.dto;

import java.time.LocalDateTime;

public class OrderMessage {
    private String orderId;
    private String userId;
    private int count;
    private String itemId;
    private LocalDateTime createTime;

    public OrderMessage() {}

    public OrderMessage(String orderId, String userId, int count, String itemId) {
        this.orderId = orderId;
        this.userId = userId;
        this.count = count;
        this.itemId = itemId;
        this.createTime = LocalDateTime.now();
    }

    // getter和setter方法...
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}