package com.charlie.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

/**
 * @PackageName: com.charlie.model
 * @Author 彭仁杰
 * @Date 2025/4/26 17:09
 * @Description
 **/
@Data
@AllArgsConstructor
public class NftTransferEventData {
    private String transactionHash;
    private String from;
    private String to;
    private BigInteger tokenId;
}
