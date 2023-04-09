package com.mundane.mail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "news")
public class WeiMiShuConfig {
    private String key;
    private List<String> groups;
}
