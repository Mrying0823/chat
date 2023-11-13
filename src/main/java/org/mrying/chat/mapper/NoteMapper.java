package org.mrying.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.mrying.chat.model.Note;

/**
 * @author 邓和颖
 */
@Mapper
public interface NoteMapper {
    int deleteByPrimaryKey(String noteId);

    int insert(Note record);

    int insertSelective(Note record);

    Note selectByPrimaryKey(String noteId);

    int updateByPrimaryKeySelective(Note record);

    int updateByPrimaryKeyWithBLOBs(Note record);

    int updateByPrimaryKey(Note record);

    // 根据 category_id 删除与笔记专题关联的笔记
    int deleteNoteByCategoryId(String categoryId);
}