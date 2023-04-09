package com.mundane.mail.task;


import com.mundane.mail.pojo.Weather;
import com.mundane.mail.service.PicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PictureTask {

    @Autowired
    private PicService picService;

    @Scheduled(cron = "0 0 19 * * ?")
    public void cron() {
        picService.collectPicList();
    }
}
