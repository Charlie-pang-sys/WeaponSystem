package com.charlie.timer;

import com.charlie.pojo.entity.OrderDO;
import com.charlie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TimerStatistics {

    @Resource
    private OrderService orderService;
    //可添加到配置文件中
    @Scheduled(cron = "0 0/5 * * * ?")
    public void statistics() {
        //todo  redis查询
        String start = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(start+"开始统计订单数据");
        String end = LocalDateTime.now().minusMinutes(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<OrderDO> orderDOList = orderService.selectByParam(end,start);
        if(CollectionUtils.isEmpty(orderDOList)){
            log.info("{}至{}时间暂无新数据",start,end);
            return;
        }
        List<OrderDO> createList = new ArrayList<>();
        List<OrderDO> completedList = new ArrayList<>();
        for (OrderDO orderDO : orderDOList) {
            int status = orderDO.getStatus();
            if(status==0){
                createList.add(orderDO);
            }else{
                completedList.add(orderDO);
            }
        }
        //已下单的订单数
        log.info("{}至{}时间查询到{}条《新创建》的订单",start,end,createList.size());
        //已完成的订单数
        log.info("{}至{}时间查询到{}条《已完成》的订单",start,end,completedList.size());

    }
}
