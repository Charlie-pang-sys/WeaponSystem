package com.charlie.listener;

import com.charlie.processor.TransactionProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @PackageName: com.charlie.listener
 * @Author 彭仁杰
 * @Date 2025/4/26 17:03
 * @Description
 **/
@Component
@Slf4j
public class BlockListener {

    private final Web3j web3j;
    private final TransactionProcessor transactionProcessor;

    public BlockListener(Web3j web3j, TransactionProcessor transactionProcessor) {
        this.web3j = web3j;
        this.transactionProcessor = transactionProcessor;
    }

    @PostConstruct
    public void start() {
        web3j.blockFlowable(true).subscribe(
                block -> {
                    block.getBlock().getTransactions().forEach(txObj -> {
                        //Object o = txObj.get();
                        //log.info("收到监听消息：{}", JSON.toJSONString(o));
                        Transaction transaction = (Transaction) txObj.get();
                        //String hash = transaction.getHash();
                        BigInteger blockNumber = transaction.getBlockNumber();
                        String blockTime = "";
                        if(Objects.nonNull(blockNumber)){
                            try {
                                EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), false).send();
                                BigInteger blockTimestramp = ethBlock.getBlock().getTimestamp();
                                // 转成人类可读的时间
                                long timestampSeconds = blockTimestramp.longValue();
                                Instant instant = Instant.ofEpochSecond(timestampSeconds);
                                LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
                                blockTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        transactionProcessor.process(transaction,blockTime);
                    });
                },
                error -> {
                    log.error("监听异常：{}", error);
                }
        );
    }
}

