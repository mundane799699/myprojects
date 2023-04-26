package com.mundane.mail;


import com.mundane.mail.mapper.PicMapper;
import com.mundane.mail.pojo.Picture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class PicDaoServiceTests {

    @Autowired
    private PicMapper picMapper;

    @Test
    public void testDeleteAll() {
        picMapper.delete(new Picture());
    }

    @Test
    public void testSelect() {
        Picture result = picMapper.findById(1);
        log.info("result = {}", result);
    }
}
