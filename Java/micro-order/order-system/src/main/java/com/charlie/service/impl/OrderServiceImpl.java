package com.charlie.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charlie.constant.TopicConstant;
import com.charlie.dao.OrderDao;
import com.charlie.pojo.dto.OrderDTO;
import com.charlie.pojo.dto.UpdateOrderStatusDTO;
import com.charlie.pojo.entity.OrderDO;
import com.charlie.service.OrderService;
import com.charlie.util.Id;
import com.charlie.util.KafkaUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.charlie.constant.RedisKeyConstant.ORDER_CREATE_KEY;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderDO> implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper JSON = new ObjectMapper();
    /**
     * 该方法只考虑订单创建
     * @param orderDTO 订单信息
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> batchCreate(OrderDTO orderDTO){
        printLog(orderDTO);
        String userId = orderDTO.getUserId();
        if(StrUtil.isEmpty(userId)){
            throw new RuntimeException("用户ID不能为空");
        }
        List<OrderDO> orderList = orderDTO.getOrders().stream().map(order -> {
            int count = order.getCount();
            if (count <= 0) {
                throw new RuntimeException("商品数量必须大于0");
            }
            String itemId = order.getItemId();
            return buildOrderDO(userId, itemId, count);
        }).collect(Collectors.toList());
        saveBatch(orderList);
        //下单的数量+1
        stringRedisTemplate.opsForValue().increment(ORDER_CREATE_KEY);
        return ResponseEntity.ok("success");
    }

    private void printLog(OrderDTO orderDTO) {
        try {
            String jsonInfo = JSON.writeValueAsString(orderDTO);
            log.info("接收到订单信息:{}",jsonInfo);
        } catch (JsonProcessingException e) {
            log.error("json解析失败:{}",e.getMessage());
            throw new RuntimeException("接收到订单信息的json解析失败:"+e.getMessage());
        }
    }

    @Override
    public ResponseEntity<OrderDO> selectByOrderId(String orderId) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getOrderId,orderId);
        OrderDO orderDO = orderDao.selectOne(wrapper);
        return ResponseEntity.ok(orderDO);
    }

    @Override
    public ResponseEntity<OrderDO> selectByUserId(String userId) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getUserId,userId);
        OrderDO orderDO = orderDao.selectOne(wrapper);
        return ResponseEntity.ok(orderDO);
    }

    @Override
    public ResponseEntity<String> updateOrderStatus(UpdateOrderStatusDTO updateOrderStatusDTO) throws JsonProcessingException {
        String orderId = updateOrderStatusDTO.getOrderId();
        int status = updateOrderStatusDTO.getStatus();
        Map<Object, Object> map = new HashMap<>();
        map.put("orderId",orderId);
        map.put("status",status);
        KafkaUtil.send(TopicConstant.ORDER_STATUS_TOPIC,JSON.writeValueAsString(map));
        return ResponseEntity.ok("更新成功");
    }

    /**
     * 构造Order实体类
     * @param userId 用户id
     * @param itemId 商品id
     * @param count 购买数量
     * @return OrderDO
     */
    private OrderDO buildOrderDO(String userId, String itemId, int count){
        OrderDO orderDO = new OrderDO();
        orderDO.setId(Id.getId());
        orderDO.setOrderId(Id.getIdStr());
        orderDO.setUserId(userId);
        orderDO.setItemId(itemId);
        orderDO.setCount(count);
        orderDO.setStatus(0);
        return orderDO;
    }
}
