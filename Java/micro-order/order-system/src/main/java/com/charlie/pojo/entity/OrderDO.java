package com.charlie.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("order_info")
@Data
public class OrderDO {
    @TableId
    private Long id;

    private String orderId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 商品id
     */
    private String itemId;
    /**
     * 商品数量
     */
    private int count;
    private int status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
