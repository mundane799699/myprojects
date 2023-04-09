package com.mundane.mail;


import com.mundane.mail.mapper.PicMapper;
import com.mundane.mail.pojo.Picture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PicDaoServiceTests {

    @Autowired
    private PicMapper picMapper;

    @Test
    public void testDeleteAll() {
        picMapper.delete(new Picture());
    }
}
