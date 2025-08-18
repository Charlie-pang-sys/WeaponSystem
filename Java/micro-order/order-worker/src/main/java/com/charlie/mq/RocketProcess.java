package com.charlie.mq;

import com.alibaba.fastjson.JSON;
import com.charlie.constant.RedisKeyConstant;
import com.charlie.pojo.entity.OrderDO;
import com.charlie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RocketMQMessageListener(
        topic = "order-topic",
        consumerGroup = "order-worker-consumer"
)
public class RocketProcess implements RocketMQListener<String> {
    @Resource
    private OrderService orderService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(String msg) {
        try {
            log.info("接收到消息{}", msg);
            OrderDO orderDO = JSON.parseObject(msg, OrderDO.class);
            orderDO.setCreateTime(LocalDateTime.now());
            orderDO.setUpdateTime(LocalDateTime.now());
            //redis也可以结合布隆过滤器，此处的幂等检查是双重的，redis会检查，
            //mysql也会检查，orderId作为唯一索引。
            if (process(orderDO)) {
                log.warn("订单已处理，订单号：{}", orderDO.getOrderId());
                return;
            }
            // 更新订单状态

            orderService.insert(orderDO);
            deleteRedisKey(orderDO.getOrderId());
            //此处的订单更新最好是发送到另一个topic，避免消息处理失败时，导致上面的数据重复处理
            orderService.updateOrderStatus(orderDO.getOrderId(),1);
            orderDO.setStatus(1);
            orderService.saveOrderToRedis(orderDO);
        } catch (Exception e) {
            log.error("订单消费者处理消息异常:{}", JSON.toJSONString(msg));
            e.printStackTrace();
        }
    }

    private void deleteRedisKey(String orderId) {
        stringRedisTemplate.delete(RedisKeyConstant.ORDER_ID_KEY+orderId);
        log.debug("幂等key已删除：{}", orderId);
    }

    private boolean process(OrderDO orderDO) {
        String key = RedisKeyConstant.ORDER_ID_KEY + orderDO.getOrderId();
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(key, orderDO.getOrderId(), Duration.ofHours(1));
        return !result;  // true表示已存在，false表示新设置
    }
}