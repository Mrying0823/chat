package org.mrying.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mrying.chat.model.Note;
import org.springframework.security.core.parameters.P;

import java.util.List;

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

    // 创建笔记
    int insertNewNote(Note note);

    // 根据 noteId 删除笔记
    int deleteNoteById(String noteId);

    // 根据 noteId 修改笔记名称
    int updateNoteTitleById(@Param("noteId") String noteId, @Param("noteTitle") String noteTitle);

    // 根据笔记专题 Id 查询其所有关联笔记
    List<Note> selectNoteByCategoryId(String categoryId);
}