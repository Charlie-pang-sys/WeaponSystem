package com.charlie.processor;

import com.charlie.model.NftTransferEventData;
import com.charlie.storage.InMemoryDatabase;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;

/**
 * @PackageName: com.charlie.processor
 * @Author 彭仁杰
 * @Date 2025/4/26 17:07
 * @Description
 **/
@Component
public class NftEventProcessor {

    private final InMemoryDatabase database;

    public NftEventProcessor(InMemoryDatabase database) {
        this.database = database;
    }

    public void process(Log log) {
        String from = "0x" + log.getTopics().get(1).substring(26);
        String to = "0x" + log.getTopics().get(2).substring(26);
        BigInteger tokenId = new BigInteger(log.getTopics().get(3).substring(2), 16);

        NftTransferEventData data = new NftTransferEventData(
                log.getTransactionHash(),
                from,
                to,
                tokenId
        );
        database.saveNftTransfer(data);
    }
}

