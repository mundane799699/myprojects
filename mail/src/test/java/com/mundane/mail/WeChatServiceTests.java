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
        String imgUrl = "https://www.ikanins.com/wp-content/uploads/2023/02/www.ikanins.com-josephine-xuan-276320962-1182215499256171-3542951419705338970-n-940x1174.jpg";
        weChatService.sendImg(imgUrl, "有钱才算自由");
    }

    @Test
    public void testSendText() {
        String text = "测试";
        weChatService.sendText(text, "有钱才算自由");
    }
}
