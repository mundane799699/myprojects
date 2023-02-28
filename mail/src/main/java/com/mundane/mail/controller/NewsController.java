package com.mundane.mail.controller;

import com.mundane.mail.service.NewsService;
import com.mundane.mail.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private NewsService newsService;

    @GetMapping("/send")
    public String sendWeatherReport() {
        try {
            String news = newsService.getNews();
            weChatService.sendToRoom(news, "有钱才算自由");
        } catch (Exception e) {
            e.printStackTrace();
            return "发送失败";
        }
        return "发送成功";
    }
}
