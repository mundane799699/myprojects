package com.mundane.mail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mail.weather")
public class WeatherConfig {
    private String apiUrl;
    private String apiKey;
}
