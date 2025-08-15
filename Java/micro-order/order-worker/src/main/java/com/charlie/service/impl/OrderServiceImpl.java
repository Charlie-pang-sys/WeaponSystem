package com.charlie.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charlie.dao.OrderDao;
import com.charlie.pojo.entity.OrderDO;
import com.charlie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderDO> implements OrderService {

    @Resource
    private OrderDao orderDao;


    @Transactional
    @Override
    public ResponseEntity<String> updateOrderStatus(String orderId,int status){
        //订单一定存在
        UpdateWrapper<OrderDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_id",orderId);
        updateWrapper.set("status",status);
        orderDao.update(null,updateWrapper);
        log.info("修改订单状态：{}", orderId);
        return ResponseEntity.ok("更新成功");
    }
}
