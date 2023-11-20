package org.mrying.chat.service;

import org.mrying.chat.model.Note;

import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/11/15 21:00
 */
public interface NoteService {
    // 新建笔记
    String createNewNote(Note note);

    // 获取与笔记专题关联的所有笔记
    List<Note> queryNoteByCategoryId(String categoryId);

    // 根据 noteId 删除笔记
    int deleteNoteById(String noteId);

    // 根据 noteId 查询笔记并修改标题
    int editNoteTitleById(String noteId,String noteTitle);

    // 更新笔记内容
    int updateNoteContentById(String noteId,String noteContent);
}
