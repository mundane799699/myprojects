package com.mundane.mail.service;

import com.mundane.mail.config.MailUserConfig;
import com.mundane.mail.pojo.MailUser;
import com.mundane.mail.pojo.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;


@Service
@EnableConfigurationProperties(MailUserConfig.class)
public class ScheduleService {

    @Autowired
    private MailService mailService;

    @Autowired
    private MailUserConfig mailUserConfig;

    public void send(Weather weather) {
        String text = String.format("今天是%s, 天气%s, 最高温度%d, 最低温度%d",
                weather.getDate(),
                weather.getWeather(),
                weather.getMaxTemperature(),
                weather.getMinTemperature());
        mailService.sendSimpleMail(mailUserConfig.getList(), text);
    }

}
