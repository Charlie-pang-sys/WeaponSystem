package com.charlie.mq;

import com.charlie.constant.TopicConstant;
import com.charlie.pojo.entity.AlarmDO;
import com.charlie.service.AlarmService;
import com.charlie.service.LarkAlarmService;
import com.charlie.util.Id;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class KafkaProcess {

    private static final ObjectMapper JSON = new ObjectMapper();

    @Resource
    private LarkAlarmService larkAlarmService;
    @Resource
    private AlarmService alarmService;

    @KafkaListener(topics = TopicConstant.ORDER_STATUS_TOPIC, groupId = "alarm-order")
    public void process(List<ConsumerRecord<String, String>> consumerRecords, Acknowledgment ack) {
        List<AlarmDO> alarmDOList = new ArrayList<>();
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            String message = consumerRecord.value();
            log.info("接收订单状态异常的消息：{}", message);
            larkAlarmService.sendAlert(message);
            AlarmDO alarmDO = buildAlarmDO(message);
            alarmDOList.add(alarmDO);
        }
        //此处可以优化为定时器定时存储
        alarmService.insertBatch(alarmDOList);
        ack.acknowledge();
    }

    private AlarmDO buildAlarmDO(String message){
        AlarmDO alarmDO = new AlarmDO();
        alarmDO.setId(Id.getId());
        alarmDO.setLog(message);
        alarmDO.setSystemName("order-system");
        return alarmDO;
    }
}
