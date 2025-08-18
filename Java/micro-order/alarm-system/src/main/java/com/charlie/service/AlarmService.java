package com.charlie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charlie.pojo.entity.AlarmDO;

import java.util.List;

public interface AlarmService extends IService<AlarmDO> {
    boolean insertBatch(List<AlarmDO> alarmDOList);

    boolean insert(AlarmDO alarmDO);
}
