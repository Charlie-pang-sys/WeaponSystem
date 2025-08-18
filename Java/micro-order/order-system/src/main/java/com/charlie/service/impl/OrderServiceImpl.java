package com.charlie.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charlie.constant.TopicConstant;
import com.charlie.dao.OrderDao;
import com.charlie.pojo.dto.OrderDTO;
import com.charlie.pojo.dto.OrderMessage;
import com.charlie.pojo.dto.UpdateOrderStatusDTO;
import com.charlie.pojo.entity.OrderDO;
import com.charlie.service.LocalTransactionMessageService;
import com.charlie.service.OrderService;
import com.charlie.util.Id;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderDO> implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private LocalTransactionMessageService localTransactionMessageService;
    /**
     * 该方法只考虑订单创建
     *
     * @param orderDTO 订单信息
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> batchCreate(OrderDTO orderDTO) {
        printLog(orderDTO);
        String userId = orderDTO.getUserId();
        if (StrUtil.isEmpty(userId)) {
            throw new RuntimeException("用户ID不能为空");
        }
        List<OrderMessage> orderMessageList = getOrderMessage(orderDTO, userId);
        // 发送到RocketMQ
        for (OrderMessage message : orderMessageList) {
            Message<OrderMessage> msg = MessageBuilder.withPayload(message).build();
            // 发送事务消息
            TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction(TopicConstant.ORDER_TOPIC, msg, null);
            if (transactionSendResult.getSendStatus().name().equals("SEND_OK")) {
                String orderId = message.getOrderId();
                log.info("订单消息发送成功: {}", orderId);
            } else {
                log.error("订单消息发送失败: {}", message.getOrderId());
            }
        }
        return ResponseEntity.ok("success");
    }

    private List<OrderMessage> getOrderMessage(OrderDTO orderDTO, String userId) {
        List<OrderMessage> orderMessageList = orderDTO.getOrders().stream().map(order -> {
            int count = order.getCount();
            if (count <= 0) {
                throw new RuntimeException("商品数量必须大于0");
            }
            String itemId = order.getItemId();
            if (StrUtil.isEmpty(itemId)) {
                throw new RuntimeException("商品ID不能为空");
            }
            return buildOrderMessage(userId, itemId, count);
        }).collect(Collectors.toList());
        return orderMessageList;
    }

    private void printLog(OrderDTO orderDTO) {
        String jsonInfo = JSON.toJSONString(orderDTO);
        log.info("接收到订单信息:{}", jsonInfo);
    }

    @Override
    public ResponseEntity<OrderDO> selectByOrderId(String orderId) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getOrderId, orderId);
        OrderDO orderDO = orderDao.selectOne(wrapper);
        return ResponseEntity.ok(orderDO);
    }

    @Override
    public ResponseEntity<List<OrderDO>> selectByUserId(String userId) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getUserId, userId);
        List<OrderDO> orderDOs = orderDao.selectList(wrapper);
        return ResponseEntity.ok(orderDOs);
    }

    @Override
    public ResponseEntity<String> updateOrderStatus(UpdateOrderStatusDTO updateOrderStatusDTO) throws JsonProcessingException {
        String orderId = updateOrderStatusDTO.getOrderId();
        int status = updateOrderStatusDTO.getStatus();
        Map<Object, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("status", status);
//        KafkaUtil.send(TopicConstant.ORDER_STATUS_TOPIC,JSON.writeValueAsString(map));
        return ResponseEntity.ok("更新成功");
    }

    /**
     * 构造Order实体类
     *
     * @param userId 用户id
     * @param itemId 商品id
     * @param count  购买数量
     * @return OrderDO
     */
    private OrderMessage buildOrderMessage(String userId, String itemId, int count) {
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId(getOrderId(userId, itemId, count));
        orderMessage.setUserId(userId);
        orderMessage.setItemId(itemId);
        orderMessage.setCount(count);
        return orderMessage;
    }

    private String getOrderId(String userId, String itemId, int count) {
        return "order_" + userId + "_" + itemId + "_" + count + Id.getIdStr();
    }
}
