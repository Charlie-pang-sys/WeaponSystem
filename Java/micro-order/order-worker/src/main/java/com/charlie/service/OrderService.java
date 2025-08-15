package com.charlie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charlie.pojo.entity.OrderDO;
import org.springframework.http.ResponseEntity;

public interface OrderService extends IService<OrderDO> {

    ResponseEntity<String> updateOrderStatus(String orderId,int status);
}
