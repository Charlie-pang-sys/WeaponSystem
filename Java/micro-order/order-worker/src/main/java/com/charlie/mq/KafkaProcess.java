package com.charlie.mq;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.charlie.constant.RedisKeyConstant;
import com.charlie.constant.TopicConstant;
import com.charlie.dao.OrderDao;
import com.charlie.pojo.entity.OrderDO;
import com.charlie.util.KafkaUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class KafkaProcess {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private OrderDao orderDao;

    private static final ObjectMapper JSON = new ObjectMapper();

    @KafkaListener(topics = TopicConstant.ORDER_STATUS_TOPIC, groupId = "order-status")
    public void process(@Payload List<ConsumerRecord<String, String>> consumerRecords, Acknowledgment ack) throws JsonProcessingException {
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            String message = consumerRecord.value();
            log.info("接收订单状态修改的消息：{}", message);
            Tuple2<String, String> tuple2 = convertToMap(message);
            String orderId = tuple2.getT1();
            String status = tuple2.getT2();
            try {
                // 1% 的概率抛出异常（模拟处理失败）
                if (RandomUtils.nextFloat() < 0.01f) {
                    log.warn("订单 {} 触发模拟异常（1% 概率）", orderId);
                    throw new RuntimeException("模拟订单处理异常：系统临时故障");
                }
                updateOrderStatus(orderId,status);
                //已完成的订单数+1
                if(status.equals("1")){
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

    private Tuple2<String, String> convertToMap(String message) throws JsonProcessingException {
        Map map = JSON.readValue(message, Map.class);
        String orderId = map.get("orderId").toString();
        String status = map.get("status").toString();
        return Tuples.of(orderId, status);
    }

    private boolean updateOrderStatus(String orderId,String status) {
        //订单一定存在
        LambdaUpdateWrapper<OrderDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(OrderDO::getOrderId,orderId);
        updateWrapper.set(OrderDO::getStatus,status);
        orderDao.update(null,updateWrapper);
        log.info("修改订单状态：{}", orderId);
        return true;
    }
}
