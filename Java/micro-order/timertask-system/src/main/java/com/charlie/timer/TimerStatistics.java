package com.charlie.timer;

import cn.hutool.core.util.StrUtil;
import com.charlie.constant.RedisKeyConstant;
import com.charlie.pojo.entity.OrderStatusDO;
import com.charlie.service.OrderStatusService;
import com.charlie.util.Id;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimerStatistics {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private OrderStatusService orderStatusService;
    //可添加到配置文件中
    @Scheduled(cron = "0 0/5 0 * * ?")
    public void statistics() {
        //todo  redis查询
        String start = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(start+"开始统计订单数据");

        //已下单的订单数
        String createCountStr = stringRedisTemplate.opsForValue().getAndDelete(RedisKeyConstant.ORDER_CREATE_KEY);
        int createCount = 0;
        if(StrUtil.isNotEmpty(createCountStr)){
            createCount = Integer.parseInt(createCountStr);
        }
        //已完成的订单数
        String completedCountStr = stringRedisTemplate.opsForValue().getAndDelete(RedisKeyConstant.ORDER_COMPLETED_KEY);
        int completedCount = 0;
        if(StrUtil.isNotEmpty(completedCountStr)){
            completedCount = Integer.parseInt(completedCountStr);
        }
        orderStatusService.insert(new OrderStatusDO(Id.getId(),LocalDateTime.now(),createCount,completedCount));
    }
}
