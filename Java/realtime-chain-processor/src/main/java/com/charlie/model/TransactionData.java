package com.charlie.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @PackageName: com.charlie.model
 * @Author 彭仁杰
 * @Date 2025/4/26 17:05
 * @Description
 **/
@Data
@AllArgsConstructor
public class TransactionData {
    private String hash;
    private String from;
    private String to;
    private String value;
}

