package com.charlie.service.impl;

import com.charlie.config.LarkWebhookProperties;
import com.charlie.pojo.entity.LarkMessageDO;
import com.charlie.service.LarkAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
@Slf4j
public class LarkAlarmServiceImpl implements LarkAlarmService {
    @Resource
    private RestTemplate restTemplate;

    @Autowired
    private LarkWebhookProperties larkWebhookProperties;

    /**
     * 发送告警消息
     */
    public boolean sendAlert(String message) {
        String str = "订单消息异常！时间：" + LocalDateTime.now()+"《"+message+"》";
        String webhookUrl = larkWebhookProperties.getAlert();
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            log.warn("Lark webhook URL 未配置");
            return false;
        }
        // 构建消息体
        LarkMessageDO msg = new LarkMessageDO("[⚠系统告警]\n" + str);
        try {
            // 使用 RestTemplate 发送 POST 请求
            ResponseEntity<String> response = restTemplate.postForEntity(
                    webhookUrl,
                    msg,
                    String.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Lark告警发送成功: {}", response.getBody());
                return true;
            } else {
                log.warn("Lark告警发送失败，HTTP 状态码: {}，消息内容:{}", response.getStatusCode(),msg);
                return false;
            }
        } catch (Exception e) {
            log.error("发送Lark告警消息异常", e);
            return false;
        }
    }
}
