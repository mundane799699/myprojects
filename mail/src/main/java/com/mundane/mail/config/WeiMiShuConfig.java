package com.mundane.mail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "news")
public class WeiMiShuConfig {
    private String key;
}
