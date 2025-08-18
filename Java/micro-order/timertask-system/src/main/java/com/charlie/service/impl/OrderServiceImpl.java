package com.charlie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charlie.dao.OrderDao;
import com.charlie.pojo.entity.OrderDO;
import com.charlie.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderDO> implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Override
    public List<OrderDO> selectByParam(String start, String end) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(OrderDO::getCreateTime,start,end);
        return orderDao.selectList(wrapper);
    }
}
