package com.mundane.mail.config;

import com.mundane.mail.pojo.MailUser;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "mail.user")
public class MailUserConfig {
    private List<MailUser> list;
}
