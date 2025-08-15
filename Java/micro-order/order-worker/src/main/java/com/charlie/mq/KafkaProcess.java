package com.charlie.mq;

import com.charlie.constant.RedisKeyConstant;
import com.charlie.constant.TopicConstant;
import com.charlie.pojo.entity.OrderDO;
import com.charlie.service.OrderService;
import com.charlie.util.KafkaUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class KafkaProcess {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private OrderService orderService;

    private static final ObjectMapper JSON = new ObjectMapper();

    public static int flagNum = 0;

    @KafkaListener(topics = TopicConstant.ORDER_STATUS_TOPIC, groupId = "order-status")
    public void process(@Payload List<ConsumerRecord<String, String>> consumerRecords, Acknowledgment ack) throws JsonProcessingException {
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            String message = consumerRecord.value();
            log.info("接收订单状态修改的消息：{}", message);
            OrderDO orderDO = convertToOrder(message);
            String orderId = orderDO.getOrderId();
            int status = orderDO.getStatus();
            try {
                if (flagNum % 3 == 0) {
                    log.warn("订单 {} 触发模拟异常（3% 概率）", orderId);
                    throw new RuntimeException("模拟订单处理异常：系统临时故障");
                }
                // 1% 的概率抛出异常（模拟处理失败）
                if (RandomUtils.nextFloat() < 0.01f) {
                    log.warn("订单 {} 触发模拟异常（1% 概率）", orderId);
                    throw new RuntimeException("模拟订单处理异常：系统临时故障");
                }
                //未判断订单状态是否已经等于1的情况
                orderService.updateOrderStatus(orderId,status);
                //已完成的订单数+1,
                if(status==1){
                    stringRedisTemplate.opsForValue().increment(RedisKeyConstant.ORDER_COMPLETED_KEY);
                }
                log.info("订单状态修改成功");
            } catch (Exception e) {
                // 记录异常订单
                log.error("订单 {} 处理失败: {}", orderId, e.getMessage());
                // 发送告警
                KafkaUtil.send(TopicConstant.ORDER_ALARM_TOPIC, message);
            }
        }
        ack.acknowledge();
    }

    private OrderDO convertToOrder(String message) throws JsonProcessingException {
        return JSON.readValue(message, OrderDO.class);
    }
}
