package com.charlie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charlie.pojo.entity.LocalTransactionMessageDO;

import java.util.Collection;

public interface LocalTransactionMessageService extends IService<LocalTransactionMessageDO> {

    LocalTransactionMessageDO getOneByOrderId(String orderId);

    boolean insertBatch(Collection<LocalTransactionMessageDO> entityList, int batchSize);

    int insert(LocalTransactionMessageDO localTransactionMessageDO);
    boolean updateStatus(String orderId, int status);
}
