package com.charlie.pojo.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UpdateOrderStatusDTO {
    @NotNull(message = "订单ID不能为空")
    private String orderId;
    @NotNull(message = "订单状态不能为空")
    @Min(value = 0, message = "订单状态不能小于0")
    @Max(value = 2, message = "订单状态不能大于2")
    private int status;
}
