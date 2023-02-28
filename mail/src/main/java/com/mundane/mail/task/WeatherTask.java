package com.mundane.mail.task;

import com.mundane.mail.pojo.Weather;
import com.mundane.mail.service.ScheduleService;
import com.mundane.mail.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class WeatherTask {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private WeatherService weatherService;

    @Scheduled(cron = "0 0 7 * * ?")
    public void cron() {
        try {
            Weather weather = weatherService.queryWeather("杭州");
            String weatherStr = weather.getWeather();
            if (weatherStr.contains("雨") || weatherStr.contains("雪") || weatherStr.contains("雷")) {
                scheduleService.send(weather);
                System.out.println("发送成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发送失败");
        }
    }
}
