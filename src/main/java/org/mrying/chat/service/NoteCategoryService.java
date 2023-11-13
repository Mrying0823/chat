package org.mrying.chat.service;

import org.mrying.chat.model.Conversation;
import org.mrying.chat.model.NoteCategory;

import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/11/13 11:03
 */
public interface NoteCategoryService {
    // 新建笔记专题
    String createNoteCategory(NoteCategory category);

    // 获取用户所有笔记专题
    List<NoteCategory> queryNoteCategoryByUserId(String userId);

    // 根据 id 删除笔记专题及关联笔记
    int deleteNoteCategoryById(String categoryId);

    // 根据 categoryId 查询笔记专题并修改标题
    int editCategoryTitleById(String categoryId,String categoryTitle);
}
