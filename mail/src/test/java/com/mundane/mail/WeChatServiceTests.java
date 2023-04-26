package com.mundane.mail;

import com.mundane.mail.service.WeChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WeChatServiceTests {
    @Autowired
    private WeChatService weChatService;

    @Test
    public void testSend() {
        String imgUrl = "https://i1.hdslb.com/bfs/archive/f950ae679a33026d1e830acab041895711819ae9.jpg";
        weChatService.sendImg(imgUrl, "有钱才算自由");
    }

    @Test
    public void testSendText() {
        String text = "测试";
        weChatService.sendText(text, "有钱才算自由");
    }
}
