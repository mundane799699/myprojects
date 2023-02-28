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
        String msg = "测试";
        weChatService.sendToRoom(msg, "有钱才算自由");
    }
}
