package com.mundane.mail.task;

import com.mundane.mail.pojo.Weather;
import com.mundane.mail.service.NewsService;
import com.mundane.mail.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NewsTask {

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private NewsService newsService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void newsCron() {
        String news = newsService.getNews();
        weChatService.sendToRoom(news, "有钱才算自由");
    }
}
