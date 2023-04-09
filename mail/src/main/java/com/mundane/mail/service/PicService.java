package com.mundane.mail.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReUtil;
import com.mundane.mail.pojo.Picture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PicService {
    @Autowired
    private PicCrawlerService crawlerService;

    @Autowired
    private PicDaoService daoService;

    @Autowired
    private MailService mailService;

    @Transactional(rollbackFor = Exception.class)
    public void collectPicList() {
        String title = crawlerService.getTitle();
        String titleInDb = daoService.selectTitle();
        if (StringUtils.equals(title, titleInDb)) {
            log.info("图集无需更新");
            return;
        }
        List<Picture> picList = crawlerService.getPicList();
        if (CollectionUtil.isNotEmpty(picList)) {
            daoService.removeAll();
            daoService.savePicList(picList);
            mailService.reportPictureUpdated();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public List<String> getPicUrlList(String word) {
        Integer number = 1;
        try {
            number = ReUtil.getFirstNumber(word);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (number == null || number > 10) {
            number = 1;
        }

        Picture checkedPic = daoService.selectCheckedPic();
        checkedPic.setStatus(0);

        Long checkedNum = checkedPic.getId();
        int totalCount = daoService.selectCount();
        Long nextCheckedNum = checkedNum + number;
        List<Picture> list = new ArrayList<>();
        if (nextCheckedNum > totalCount) {
            nextCheckedNum = nextCheckedNum % totalCount;
            // 发送checkedNum到totalCount，以及1到nextNum-1之间的pic
            Example example = new Example(Picture.class);
            example.createCriteria().andBetween("id", checkedNum, totalCount);
            List<Picture> part1 = daoService.selectByExample(example);
            list.addAll(part1);
            if (nextCheckedNum > 1) {
                example = new Example(Picture.class);
                example.createCriteria().andBetween("id", 1, nextCheckedNum - 1);
                List<Picture> part2 = daoService.selectByExample(example);
                list.addAll(part2);
            }
        } else {
            // 发送checkedNum到nextNum - 1之间的pic
            Example example = new Example(Picture.class);
            example.createCriteria().andBetween("id", checkedNum, nextCheckedNum - 1);
            List<Picture> pictures = daoService.selectByExample(example);
            list.addAll(pictures);
        }
        List<String> returnList = new ArrayList<>();
        for (Picture picture : list) {
            returnList.add(picture.getUrl());
        }

        daoService.updatePicById(nextCheckedNum);
        daoService.updateByPrimaryKey(checkedPic);

        return returnList;
    }
}
