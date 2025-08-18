package com.charlie.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("local_transaction_message")
public class LocalTransactionMessageDO {
    @TableId
    private Long id;
    private String orderId;          // 订单ID
    private String topic;            // MQ主题
    private String messageBody;      // 消息体
    private Integer status;          // 状态：0-待发送，1-已发送，2-发送失败，3-消费完成
    private LocalDateTime createTime;         // 创建时间
    private LocalDateTime updateTime;         // 更新时间

    // 状态常量
    @TableField(exist = false)
    public static final int STATUS_PENDING = 0;      // 待发送
    @TableField(exist = false)
    public static final int STATUS_SENT = 1;         // 已发送
    @TableField(exist = false)
    public static final int STATUS_SEND_FAILED = 2;  // 发送失败
    @TableField(exist = false)
    public static final int STATUS_CONSUMED = 3;     // 消费完成

    public LocalTransactionMessageDO( Long id,String orderId, String orderTopic, String toJSONString, Integer s,LocalDateTime createTime,LocalDateTime updateTime) {
        this.id = id;
        this.orderId = orderId;
        this.topic = orderTopic;
        this.messageBody = toJSONString;
        this.status = s;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
