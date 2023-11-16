package org.mrying.chat.service.impl;

import org.mrying.chat.mapper.NoteMapper;
import org.mrying.chat.model.Note;
import org.mrying.chat.service.NoteService;
import org.mrying.chat.utils.SecurityContextHolderUtils;
import org.mrying.chat.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/11/15 21:05
 */
@Service("noteService")
public class NoteServiceImpl implements NoteService {

    private NoteMapper noteMapper;

    @Autowired
    public void setNoteMapper(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    // 新建笔记
    @Override
    public String createNewNote(Note note) {
        // 设置创建人
        note.setUserId(SecurityContextHolderUtils.getUserId());
        // 设置创建时间
        note.setCreateTime(new Date());
        // 设置笔记专题 ID
        note.setNoteId(UUIDUtils.getUUID());
        // 设置初始笔记专题名称
        note.setNoteTitle("新建笔记");
        // 保存创建的笔记专题
        noteMapper.insertNewNote(note);
        // 返回笔记专题 ID
        return note.getCategoryId();
    }

    // 获取笔记专题下的所有笔记
    @Override
    public List<Note> queryNoteByCategoryId(String categoryId) {
        return noteMapper.selectNoteByCategoryId(categoryId);
    }

    // 根据 Id 删除笔记
    @Override
    public int deleteNoteById(String noteId) {
        return noteMapper.deleteNoteById(noteId);
    }

    // 根据 Id 修改笔记标题
    @Override
    public int editNoteTitleById(String noteId, String noteTitle) {
        return noteMapper.updateNoteTitleById(noteId,noteTitle);
    }
}
