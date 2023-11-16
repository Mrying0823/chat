package org.mrying.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mrying.chat.model.NoteCategory;

import java.util.List;

/**
 * @author 邓和颖
 */
@Mapper
public interface NoteCategoryMapper {
    int deleteByPrimaryKey(String categoryId);

    int insert(NoteCategory record);

    int insertSelective(NoteCategory record);

    NoteCategory selectByPrimaryKey(String categoryId);

    int updateByPrimaryKeySelective(NoteCategory record);

    int updateByPrimaryKey(NoteCategory record);

    // 新建笔记专题
    int insertNewNoteCategory(NoteCategory category);

    // 根据 userId 查询与用户创建的笔记专题
    List<NoteCategory> selectNoteCategoryByUserId(String userId);

    // 根据 category_id 删除笔记专题
    int deleteNoteCategoryById(String categoryId);

    // 根据 categoryId 查询笔记专题并修改标题
    int updateCategoryTitleById(@Param("categoryId") String categoryId, @Param("categoryTitle") String categoryTitle);
}