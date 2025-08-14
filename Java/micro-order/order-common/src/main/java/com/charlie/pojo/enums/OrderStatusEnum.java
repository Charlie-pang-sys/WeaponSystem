package com.charlie.pojo.enums;

public enum OrderStatusEnum {
    NEW(0, "新订单"),
    PAID(1, "已完成")
    ;
    private int code;
    private String message;

    OrderStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode (int code){
        for (OrderStatusEnum value : OrderStatusEnum.values()) {
            if (value.code == code) {
                return value.message;
            }
        }
        return null;
    }
}
