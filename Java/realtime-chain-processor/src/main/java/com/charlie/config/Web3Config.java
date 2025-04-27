package com.charlie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * @PackageName: com.charlie.config
 * @Author 彭仁杰
 * @Date 2025/4/26 17:02
 * @Description
 **/
@Configuration
public class Web3Config {

    @Value("${web3.rpc.url}")
    private String rpcUrl;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(rpcUrl));
    }
}

