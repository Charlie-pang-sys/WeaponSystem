package com.charlie.pojo.dto;

import lombok.Data;

@Data
public class UpdateOrderStatusDTO {
    private String orderId;
    private int status;
}
