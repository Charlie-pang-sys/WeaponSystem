package com.charlie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charlie.pojo.entity.OrderDO;

import java.util.List;

public interface OrderService extends IService<OrderDO> {
    List<OrderDO> selectByParam(String start,String end);
}
