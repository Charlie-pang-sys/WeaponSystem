package com.charlie.processor;

import com.alibaba.fastjson2.JSON;
import com.charlie.model.TransactionData;
import com.charlie.storage.InMemoryDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.Transaction;

/**
 * @PackageName: com.charlie.processor
 * @Author 彭仁杰
 * @Date 2025/4/26 17:04
 * @Description
 **/
@Component
@Slf4j
public class TransactionProcessor {

    private final InMemoryDatabase database;

    public TransactionProcessor(InMemoryDatabase database) {
        this.database = database;
    }

    public void process(Transaction tx,String blockTime) {
        TransactionData data = new TransactionData(
                tx.getHash(),
                tx.getFrom(),
                tx.getTo(),
                tx.getValue().toString()
        );
        String s = JSON.toJSONString(data);
        log.info("收到消息：{},区块时间：{}",s,blockTime);
        //database.saveTransaction(data);
    }
}

