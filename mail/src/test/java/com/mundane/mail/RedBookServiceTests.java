package com.mundane.mail;

import com.mundane.mail.mapper.RedBookCollectMapper;
import com.mundane.mail.pojo.RedBookCollectEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class RedBookServiceTests {

    @Autowired
    private RedBookCollectMapper redBookCollectMapper;

    @Test
    public void testIncert() {
        RedBookCollectEntity entity = new RedBookCollectEntity();
        entity.setUserId("5f827ea10000000001007b5b");
        entity.setNoteId("6434ddd60000000013010e3f");
        entity.setOwnerId("62f447b7000000001f0176cf");
        entity.setOwnerNickname("那个大飞哥");
        entity.setOwnerAvatar("https://sns-avatar-qc.xhscdn.com/avatar/64280ed3609ee59967b89302.jpg?imageView2/2/w/120/format/jpg");
        entity.setCoverUrl("https://sns-img-hw.xhscdn.com/1000g0082at99g0ags0005onk8urnqtmfv35e0po");
        entity.setLiked(true);
        entity.setDisplayTitle("1.8万美金编程培训班有必要吗");
        entity.setType("normal");
        redBookCollectMapper.insert(entity);
    }

    @Test
    public void testSelect() {
        RedBookCollectEntity result = redBookCollectMapper.findByNoteId("6434ddd60000000013010e3f");
        log.info("result = {}", result);
    }

    @Test
    public void testBatchInsertOrUpdate() {
        List<RedBookCollectEntity> list = new ArrayList<>();
        RedBookCollectEntity entity = new RedBookCollectEntity();
        entity.setUserId("5f827ea10000000001007b5b");
        entity.setNoteId("6434ddd60000000013010e3f");
        entity.setOwnerId("62f447b7000000001f0176cf");
        entity.setOwnerNickname("hehehehhe");
        entity.setOwnerAvatar("https://sns-avatar-qc.xhscdn.com/avatar/64280ed3609ee59967b89302.jpg?imageView2/2/w/120/format/jpg");
        entity.setCoverUrl("https://sns-img-hw.xhscdn.com/1000g0082at99g0ags0005onk8urnqtmfv35e0po");
        entity.setLiked(false);
        entity.setLikedCount(35);
        entity.setDisplayTitle("rrrrr");
        entity.setType("video");
        list.add(entity);
        redBookCollectMapper.batchAddOrUpdate(list);
    }

    @Test
    public void testSelectAll() {
        List<RedBookCollectEntity> list = redBookCollectMapper.queryAllByUserId("123");
        log.info("list = {}", list);
    }
}
