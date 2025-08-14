package com.charlie.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("order_status")
public class OrderStatusDO {
    @TableId
    private Long id;

    private LocalDateTime statTime;

    private Integer newStatus;

    private int completedCount;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public OrderStatusDO(Long id, LocalDateTime statTime, Integer newStatus, int completedCount) {
        this.id = id;
        this.statTime = statTime;
        this.newStatus = newStatus;
        this.completedCount = completedCount;
    }
}
