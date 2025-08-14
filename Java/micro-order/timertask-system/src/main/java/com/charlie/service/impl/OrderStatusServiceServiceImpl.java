package com.charlie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charlie.dao.OrderStatusDao;
import com.charlie.pojo.entity.OrderStatusDO;
import com.charlie.service.OrderStatusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class OrderStatusServiceServiceImpl extends ServiceImpl<OrderStatusDao, OrderStatusDO> implements OrderStatusService {

    @Resource
    private OrderStatusDao orderStatusDao;

    @Transactional
    @Override
    public int insert(OrderStatusDO orderStatusDO) {
        return orderStatusDao.insert(orderStatusDO);
    }
}
