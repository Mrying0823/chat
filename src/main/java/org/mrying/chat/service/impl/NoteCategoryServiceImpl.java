package org.mrying.chat.service.impl;

import org.mrying.chat.mapper.NoteCategoryMapper;
import org.mrying.chat.mapper.NoteMapper;
import org.mrying.chat.model.NoteCategory;
import org.mrying.chat.service.NoteCategoryService;
import org.mrying.chat.utils.SecurityContextHolderUtils;
import org.mrying.chat.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/11/13 11:03
 */
@Service("noteCategoryService")
public class NoteCategoryServiceImpl implements NoteCategoryService {

    private NoteMapper noteMapper;

    private NoteCategoryMapper noteCategoryMapper;

    @Autowired
    public void setNoteCategoryMapper(NoteCategoryMapper noteCategoryMapper) {
        this.noteCategoryMapper = noteCategoryMapper;
    }

    @Autowired
    public void setNoteMapper(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    // 新建笔记专题
    @Override
    public String createNoteCategory(NoteCategory category) {
        // 设置创建人
        category.setUserId(SecurityContextHolderUtils.getUserId());
        // 设置创建时间
        category.setCreateTime(new Date());
        // 设置笔记专题 ID
        category.setCategoryId(UUIDUtils.getUUID());
        // 设置初始笔记专题名称
        category.setCategoryTitle("新建笔记专题");
        // 保存创建的笔记专题
        noteCategoryMapper.insertNewNoteCategory(category);
        // 返回笔记专题 ID
        return category.getCategoryId();
    }

    // 获取用户所有的笔记专题
    @Override
    public List<NoteCategory> queryNoteCategoryByUserId(String userId) {
        return noteCategoryMapper.selectNoteCategoryByUserId(userId);
    }

    // 删除笔记专题及关联笔记
    @Transactional
    @Override
    public synchronized int deleteNoteCategoryById(String categoryId) {

        int result;

        // 根据 categoryId 删除笔记专题
        result = noteCategoryMapper.deleteNoteCategoryById(categoryId);

        // 根据 categoryId 删除笔记
        if(result > 0) {
            result = noteMapper.deleteNoteByCategoryId(categoryId);
        }

        return result;
    }

    // 根据 categoryId 查询笔记专题并修改标题
    @Override
    public int editCategoryTitleById(String categoryId, String categoryTitle) {
        return noteCategoryMapper.updateCategoryTitleById(categoryId, categoryTitle);
    }
}
