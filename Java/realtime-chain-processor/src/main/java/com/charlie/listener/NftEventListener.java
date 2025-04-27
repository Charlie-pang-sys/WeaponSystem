package com.charlie.listener;

import com.charlie.processor.NftEventProcessor;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;

import java.util.Arrays;

/**
 * @PackageName: com.charlie.listener
 * @Author 彭仁杰
 * @Date 2025/4/26 17:07
 * @Description
 **/
@Component
public class NftEventListener {

    private final Web3j web3j;
    private final NftEventProcessor nftEventProcessor;

    // ERC721 Transfer事件的topic
    private static final String TRANSFER_TOPIC = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";

    public NftEventListener(Web3j web3j, NftEventProcessor nftEventProcessor) {
        this.web3j = web3j;
        this.nftEventProcessor = nftEventProcessor;
    }

    //@PostConstruct
    public void listenToNftTransfer() {
        EthFilter filter = new EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                Arrays.asList("合约地址") // 你的NFT合约地址
        );
        filter.addSingleTopic(TRANSFER_TOPIC);

        web3j.ethLogFlowable(filter).subscribe(log -> {
            nftEventProcessor.process(log);
        });
    }
}

