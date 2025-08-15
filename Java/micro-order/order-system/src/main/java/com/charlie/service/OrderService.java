package com.charlie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charlie.pojo.dto.OrderDTO;
import com.charlie.pojo.dto.UpdateOrderStatusDTO;
import com.charlie.pojo.entity.OrderDO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService extends IService<OrderDO> {
    ResponseEntity<String> batchCreate(OrderDTO orderDTO);

    ResponseEntity<OrderDO> selectByOrderId(String orderId);

    ResponseEntity<List<OrderDO>> selectByUserId(String userId);

    ResponseEntity<String> updateOrderStatus(UpdateOrderStatusDTO updateOrderStatusDTO) throws JsonProcessingException;
}
