package com.charlie.controller;

import com.charlie.pojo.dto.OrderDTO;
import com.charlie.pojo.dto.UpdateOrderStatusDTO;
import com.charlie.pojo.entity.OrderDO;
import com.charlie.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

//    @RateLimit(value = 100, timeWindow = 1)
    @PostMapping("batchCreate")
    public ResponseEntity<String> batchCreate(@Validated @RequestBody OrderDTO orderDTO){
        return orderService.batchCreate(orderDTO);
    }

    @GetMapping("/selectByOrderId/{orderId}")
    public ResponseEntity<OrderDO> selectByOrderId(@PathVariable String orderId){
        return orderService.selectByOrderId(orderId);
    }

    @GetMapping("/selectByUserId/{userId}")
    public ResponseEntity<List<OrderDO>> selectByUserId(@PathVariable String userId){
        return orderService.selectByUserId(userId);
    }

    @PostMapping("/updateOrderStatus")
    public ResponseEntity<String> updateOrderStatus(@Validated @RequestBody UpdateOrderStatusDTO updateOrderStatusDTO) throws JsonProcessingException {
        return orderService.updateOrderStatus(updateOrderStatusDTO);
    }
}
