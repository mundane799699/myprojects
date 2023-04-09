package com.mundane.mail.controller;

import com.mundane.mail.pojo.PicData;
import com.mundane.mail.pojo.WeChatRequest;
import com.mundane.mail.pojo.WeChatResp;
import com.mundane.mail.service.PicService;
import com.mundane.mail.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mail/pic")
public class PictureController {
    @Autowired
    private PicService picService;

    @Autowired
    private WeChatService weChatService;

    @GetMapping("/collect")
    public String collect() {
        picService.collectPicList();
        return "采集成功";
    }


    @PostMapping("/send")
//    @GetMapping("/send")
    public WeChatResp<List<PicData>> send(@RequestBody WeChatRequest request) {
        List<String> picUrlList = picService.getPicUrlList(request.getWord());
        log.info("picUrlList = {}", picUrlList);
        List<PicData> data = new ArrayList<>();
        for (String url : picUrlList) {
            PicData picData = new PicData();
            picData.setUrl(url);
            picData.setType(2);
            data.add(picData);
        }
        return WeChatResp.success(data);
    }
}
