package com.mundane.mail;

import com.mundane.mail.service.NewsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NewsServiceTests {

    @Autowired
    private NewsService newsService;

    @Test
    public void testGetNews() {
        String news = newsService.getNews();
        System.out.println(news);
    }
}
