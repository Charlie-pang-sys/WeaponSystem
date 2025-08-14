package com.charlie.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("alarm_info")
public class AlarmDO {
    @TableId
    private long id;

    private String systemName;

    private String log;

    //日志发送状态
    private int status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
