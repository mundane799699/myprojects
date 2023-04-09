package com.mundane.mail;

import com.mundane.mail.pojo.Picture;
import com.mundane.mail.service.PicDaoService;
import com.mundane.mail.service.PicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@SpringBootTest
public class PictureTests {

    @Autowired
    private PicService picService;

    @Autowired
    private PicDaoService daoService;


    @Test
    public void testCollect() {
        picService.collectPicList();
    }

    @Test
    public void testSelect() {
        Example example = new Example(Picture.class);
        example.createCriteria().andBetween("num", 79, 82);
        List<Picture> part1 = daoService.selectByExample(example);
        System.out.println(part1);

        example = new Example(Picture.class);
        example.createCriteria().andBetween("num", 1, 2);
        List<Picture> part2 = daoService.selectByExample(example);
        System.out.println(part2);
    }
}
