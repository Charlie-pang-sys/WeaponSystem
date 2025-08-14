package com.charlie.controller;

import com.charlie.pojo.dto.OrderDTO;
import com.charlie.pojo.dto.UpdateOrderStatusDTO;
import com.charlie.pojo.entity.OrderDO;
import com.charlie.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("batchCreate")
    public ResponseEntity<String> batchCreate(@RequestBody OrderDTO orderDTO){
        return orderService.batchCreate(orderDTO);
    }

    @GetMapping("/selectByOrderId/{orderId}")
    public ResponseEntity<OrderDO> selectByOrderId(@PathVariable String orderId){
        return orderService.selectByOrderId(orderId);
    }

    @GetMapping("/selectByUserId/{userId}")
    public ResponseEntity<OrderDO> selectByUserId(@PathVariable String userId){
        return orderService.selectByUserId(userId);
    }

    @PostMapping("/updateOrderStatus")
    public ResponseEntity<String> updateOrderStatus(@RequestBody UpdateOrderStatusDTO updateOrderStatusDTO) throws JsonProcessingException {
        return orderService.updateOrderStatus(updateOrderStatusDTO);
    }
}
