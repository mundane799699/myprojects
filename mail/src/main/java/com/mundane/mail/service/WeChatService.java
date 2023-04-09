package com.mundane.mail.service;

import cn.hutool.json.JSONObject;
import com.mundane.mail.config.WeiMiShuConfig;
import com.mundane.mail.pojo.WeiMiShuVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@EnableConfigurationProperties(WeiMiShuConfig.class)
public class WeChatService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeiMiShuConfig weiMiShuConfig;

    public static final String WEI_MI_SHU_API = "https://api-bot.aibotk.com";


    public void sendText(String content, String roomName) {
        String url = WEI_MI_SHU_API + "/openapi/v1/chat/room";
        WeiMiShuVO weiMiShuVO = new WeiMiShuVO();
        weiMiShuVO.setType(1);
        weiMiShuVO.setContent(content);
        JSONObject object = new JSONObject();
        object.set("apiKey", weiMiShuConfig.getKey());
        object.set("roomName", roomName);
        object.set("message", weiMiShuVO);
        String response = restTemplate.postForObject(url, object, String.class);
        log.info(response);

    }

    public void sendImg(String imgUrl, String roomName) {
        String url = WEI_MI_SHU_API + "/openapi/v1/chat/room";
        WeiMiShuVO weiMiShuVO = new WeiMiShuVO();
        weiMiShuVO.setType(2);
        weiMiShuVO.setUrl(imgUrl);
        JSONObject object = new JSONObject();
        object.set("apiKey", weiMiShuConfig.getKey());
        object.set("roomName", roomName);
        object.set("message", weiMiShuVO);
        String response = restTemplate.postForObject(url, object, String.class);
        log.info(response);
    }

    public void sendNewsToGroup(String news) {
        for (String roomName : weiMiShuConfig.getGroups()) {
            sendText(news, roomName);
        }
    }
}
