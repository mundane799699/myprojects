package com.mundane.mail.service;

import cn.hutool.json.JSONObject;
import com.mundane.mail.config.WeiMiShuConfig;
import com.mundane.mail.pojo.WeiMiShuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@EnableConfigurationProperties(WeiMiShuConfig.class)
public class WeChatService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeiMiShuConfig weiMiShuConfig;

    public static final String WEI_MI_SHU_API = "https://api-bot.aibotk.com";


    public void sendToRoom(String content, String roomName) {
        String url = WEI_MI_SHU_API + "/openapi/v1/chat/room";
        WeiMiShuVO weiMiShuVO = new WeiMiShuVO();
        weiMiShuVO.setType(1);
        weiMiShuVO.setContent(content);
        JSONObject object = new JSONObject();
        object.set("apiKey", weiMiShuConfig.getKey());
        object.set("roomName", roomName);
        object.set("message", weiMiShuVO);
        String response = restTemplate.postForObject(url, object, String.class);
        System.out.println(response);

    }
}
