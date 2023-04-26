package com.mundane.mail.mapper;

import com.mundane.mail.common.BaseMapper;
import com.mundane.mail.pojo.RedBookCollectEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RedBookCollectMapper extends BaseMapper<RedBookCollectEntity> {

    RedBookCollectEntity findByNoteId(@Param("id") String noteId);

    void batchAddOrUpdate(List<RedBookCollectEntity> list);

    List<RedBookCollectEntity> queryAllByUserId(@Param("userId") String userId);

    List<RedBookCollectEntity> queryByUserId(@Param("userId") String userId,
                                             @Param("displayTitle") String displayTitle,
                                             @Param("ownerNickname") String ownerNickname);

    void deleteByUserId(@Param("userId") String userId);
}
