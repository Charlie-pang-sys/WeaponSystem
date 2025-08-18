package com.charlie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charlie.pojo.entity.OrderDO;
import com.charlie.pojo.entity.PageResult;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService extends IService<OrderDO> {

//    ResponseEntity<String> updateOrderStatus(String orderId,int status);

    boolean updateOrderStatus(String orderId,int status);

    boolean insertBatch(List<OrderDO> orderDOList);

    boolean insert(OrderDO orderDO);

//    boolean batchUpdateOrderStatus(List<OrderDO> orderDOList);

    void saveOrderToRedis(OrderDO order);

    PageResult<List<OrderDO>> queryOrders(String userId,
                                          int status,
                                          Long startTime,
                                          Long endTime,
                                          int pageNum,
                                          int pageSize);

    ResponseEntity<String> batchCreate(OrderDO orderDO);
}
