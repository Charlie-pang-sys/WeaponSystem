package com.charlie.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lark.webhook")
@Data
public class LarkWebhookProperties {
    private String alert;
}
