package com.charlie.controller;

import com.charlie.service.LarkAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private LarkAlarmService larkAlarmService;

    @GetMapping("/alert")
    public String sendAlert(String message) {
        boolean sent = larkAlarmService.sendAlert("订单消息异常！时间：" + LocalDateTime.now()+"《"+message+"》");
        return sent ? "告警已发送" : "发送失败";
    }
}
