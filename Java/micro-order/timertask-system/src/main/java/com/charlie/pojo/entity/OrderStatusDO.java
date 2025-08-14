package com.charlie.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("order_stats")
public class OrderStatusDO {
    @TableId
    private Long id;

    private LocalDateTime statTime;

    private Integer newCount;

    private int completedCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
