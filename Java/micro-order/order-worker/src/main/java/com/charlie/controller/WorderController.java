package com.charlie.controller;

import com.charlie.constant.RedisKeyConstant;
import com.charlie.pojo.entity.OrderDO;
import com.charlie.pojo.entity.PageResult;
import com.charlie.service.OrderService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/worker")
public class WorderController {


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private OrderService orderService;

    @GetMapping("/queryOrders")
    public PageResult<List<OrderDO>> queryOrders(@RequestParam String userId,
                                                 @RequestParam int status,
                                                 @RequestParam Long startTime,
                                                 @RequestParam Long endTime,
                                                 @RequestParam int pageNum,
                                                 @RequestParam int pageSize){
        return orderService.queryOrders(userId, status, startTime, endTime, pageNum, pageSize);
    }

    @PostMapping("/batchCreate")
    public ResponseEntity<String> batchCreate(@RequestBody OrderDO orderDO){
        return orderService.batchCreate(orderDO);
    }

    @GetMapping("/getOrderInfo/{orderId}")
    public String getOrderInfo(@PathVariable String orderId){
        return stringRedisTemplate.opsForValue().get(RedisKeyConstant.ORDER_ID_KEY+orderId);
    }
}
