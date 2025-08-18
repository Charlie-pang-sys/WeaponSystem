package com.charlie.config;

import com.alibaba.fastjson.JSON;
import com.charlie.constant.TopicConstant;
import com.charlie.pojo.dto.OrderMessage;
import com.charlie.pojo.entity.LocalTransactionMessageDO;
import com.charlie.service.LocalTransactionMessageService;
import com.charlie.util.Id;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

@RocketMQTransactionListener()
@Slf4j
public class OrderTransactionImpl implements RocketMQLocalTransactionListener {

    @Resource
    private LocalTransactionMessageService localTransactionMessageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String body = new String((byte[]) msg.getPayload(), StandardCharsets.UTF_8);
        try {
            // 1. 解析消息内容
            OrderMessage orderMessage = JSON.parseObject(body, OrderMessage.class);

            // 2. 记录事务日志（本地事务的一部分）
            LocalTransactionMessageDO transactionLog = new LocalTransactionMessageDO(
                    Id.getId(),
                    orderMessage.getOrderId(),
                    TopicConstant.ORDER_TOPIC,
                    body,
                    LocalTransactionMessageDO.STATUS_PENDING
                    , LocalDateTime.now(),
                    LocalDateTime.now()
            );
            localTransactionMessageService.insert(transactionLog);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            log.error("发送事务消息异常!异常信息为:{}", e.getMessage());
            return RocketMQLocalTransactionState.ROLLBACK;
        }

    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String body = new String((byte[]) msg.getPayload(), StandardCharsets.UTF_8);
        OrderMessage orderMessage = JSON.parseObject(body, OrderMessage.class);
        String orderId = orderMessage.getOrderId();
        if (orderId == null || orderId.isEmpty()) {
            System.out.println("订单ID为空，回滚消息");
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        System.out.println("【回查事务状态】订单ID: " + orderId);
        try {
            // 实际应查询数据库：orderId是否存在？
            LocalTransactionMessageDO localTransactionMessageDO = localTransactionMessageService.getOneByOrderId(orderId);
            if(Objects.isNull(localTransactionMessageDO)){
                // 查不到记录，可能是插入失败，回滚
                System.out.println("未找到订单记录，回滚消息");
                return RocketMQLocalTransactionState.ROLLBACK;
            }
            if (localTransactionMessageDO.getStatus().equals(LocalTransactionMessageDO.STATUS_PENDING)) {
                System.out.println("订单存在，提交消息");
                localTransactionMessageService.updateStatus(orderId, LocalTransactionMessageDO.STATUS_SENT);
                return RocketMQLocalTransactionState.COMMIT;
            } else {
                System.out.println("订单不存在，回滚消息");
                return RocketMQLocalTransactionState.ROLLBACK;
            }
        } catch (Exception e) {
            // 数据库异常（如超时、连接失败）
            //无法确定状态，只能返回 UNKNOWN，让 Broker 稍后重试
            System.err.println("回查时数据库异常: " + e.getMessage());
            return RocketMQLocalTransactionState.UNKNOWN; //临时状态
        }
    }
}
