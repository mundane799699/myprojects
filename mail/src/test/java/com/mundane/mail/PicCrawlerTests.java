package com.mundane.mail;

import com.mundane.mail.service.PicCrawlerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PicCrawlerTests {
    @Autowired
    private PicCrawlerService picCrawlerService;

    @Test
    public void testGetPicList() {
        picCrawlerService.getPicList();
    }
}
