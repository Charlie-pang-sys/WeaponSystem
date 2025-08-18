package com.charlie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charlie.dao.AlarmDao;
import com.charlie.pojo.entity.AlarmDO;
import com.charlie.service.AlarmService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmDao, AlarmDO> implements AlarmService {


    @Override
    public boolean insertBatch(List<AlarmDO> alarmDOList) {
        return this.saveBatch(alarmDOList);
    }

    @Override
    public boolean insert(AlarmDO alarmDO) {
        return this.save(alarmDO);
    }
}
