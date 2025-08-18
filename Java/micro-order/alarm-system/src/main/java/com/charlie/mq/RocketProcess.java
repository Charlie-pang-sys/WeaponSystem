package com.charlie.mq;

import com.charlie.constant.TopicConstant;
import com.charlie.pojo.entity.AlarmDO;
import com.charlie.service.AlarmService;
import com.charlie.service.LarkAlarmService;
import com.charlie.util.Id;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Service
@RocketMQMessageListener(
        topic = TopicConstant.ORDER_ALARM_TOPIC,
        consumerGroup = "order-alarm-consumer"
)
public class RocketProcess implements RocketMQListener<String> {
    @Resource
    private LarkAlarmService larkAlarmService;
    @Resource
    private AlarmService alarmService;

    @Override
    public void onMessage(String message) {
        log.info("接收订单状态异常的消息：{}", message);
        boolean flag = larkAlarmService.sendAlert(message);
        AlarmDO alarmDO = buildAlarmDO(message, flag);
        //此处可以优化为定时器定时存储
        alarmService.insert(alarmDO);
    }

    private AlarmDO buildAlarmDO(String message, boolean flag) {
        AlarmDO alarmDO = new AlarmDO();
        alarmDO.setId(Id.getId());
        alarmDO.setLog(message);
        alarmDO.setSystemName("order-system");
        alarmDO.setStatus(flag ? 1 : 0);
        alarmDO.setCreateTime(LocalDateTime.now());
        alarmDO.setUpdateTime(LocalDateTime.now());
        return alarmDO;
    }
}