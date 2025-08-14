package com.charlie.util;

import com.charlie.holder.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
@Slf4j
public class KafkaUtil {
    public static void send(String topic, String data) {
        KafkaTemplate kafkaTemplate = SpringHolder.getBean(KafkaTemplate.class);
        ListenableFuture sendResult = kafkaTemplate.send(topic, data);
        sendResult.addCallback(
                new ListenableFutureCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        log.info("发送消息成功：{}", result);
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        log.error("发送消息失败：{}", ex.getMessage());
                    }
                }
        );
    }
}
