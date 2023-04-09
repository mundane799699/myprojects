package com.mundane.mail.service;

import com.mundane.mail.mapper.PicMapper;
import com.mundane.mail.pojo.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class PicDaoService {
    @Autowired
    private PicMapper picMapper;

    public void removeAll() {
        picMapper.delete(new Picture());
    }

    public void savePicList(List<Picture> picList) {
        for (Picture picture : picList) {
            picMapper.insertSelective(picture);
        }
    }

    public Picture selectCheckedPic() {
        Picture picture = new Picture();
        picture.setStatus(1);
        return picMapper.selectOne(picture);
    }

    public void updateByPrimaryKey(Picture pic) {
        picMapper.updateByPrimaryKey(pic);
    }

    public int selectCount() {
        return picMapper.selectCount(new Picture());
    }

    public List<Picture> selectByExample(Example example) {
        return picMapper.selectByExample(example);
    }

    public void updatePicById(Long num) {
        Picture picture = new Picture();
        picture.setId(num);
        Picture nextPic = picMapper.selectOne(picture);
        nextPic.setStatus(1);
        picMapper.updateByPrimaryKeySelective(nextPic);
    }

    public String selectTitle() {
        Picture picture = new Picture();
        picture.setId(1L);
        Picture result = picMapper.selectOne(picture);
        if (result == null) {
            return null;
        }
        return result.getTitle().trim();
    }
}
