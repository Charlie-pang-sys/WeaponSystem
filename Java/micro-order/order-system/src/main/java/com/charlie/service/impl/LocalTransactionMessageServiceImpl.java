package com.charlie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charlie.dao.LocalTransactionMessageDao;
import com.charlie.pojo.entity.LocalTransactionMessageDO;
import com.charlie.service.LocalTransactionMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class LocalTransactionMessageServiceImpl extends ServiceImpl<LocalTransactionMessageDao, LocalTransactionMessageDO> implements LocalTransactionMessageService {

    @Resource
    private LocalTransactionMessageDao localTransactionMessageDao;
    @Override
    public LocalTransactionMessageDO getOneByOrderId(String orderId) {
        LambdaQueryWrapper<LocalTransactionMessageDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LocalTransactionMessageDO::getOrderId, orderId);
        return localTransactionMessageDao.selectOne(wrapper);
    }

    @Override
    public boolean insertBatch(Collection<LocalTransactionMessageDO> entityList, int batchSize) {
        return this.saveBatch(entityList, batchSize);
    }

    @Override
    public int insert(LocalTransactionMessageDO localTransactionMessageDO) {
        return localTransactionMessageDao.insert(localTransactionMessageDO);
    }

    @Override
    public boolean updateStatus(String orderId, int status) {
        LambdaUpdateWrapper<LocalTransactionMessageDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LocalTransactionMessageDO::getOrderId, orderId);
        wrapper.set(LocalTransactionMessageDO::getStatus, status);
        wrapper.set(LocalTransactionMessageDO::getUpdateTime, LocalDateTime.now());
        return true;
    }
}
