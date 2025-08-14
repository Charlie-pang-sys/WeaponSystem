package com.charlie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charlie.dao.OrderStatusDao;
import com.charlie.pojo.entity.OrderStatusDO;

public interface OrderStatusService extends IService<OrderStatusDO> {
    int insert(OrderStatusDO orderStatusDO);
}
