package org.mrying.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.mrying.chat.model.Question;

import java.util.List;
import java.util.Map;

/**
 * @author 邓和颖
 */
@Mapper
public interface QuestionMapper {

    // 根据查询条件分页查询问题列表
    List<Question> selectQuestionByConditionForPage(Map<String,Object> map);

    // 根据查询条件获取问题数量
    int selectCountOfQuestionByCondition(Map<String,Object> map);

    // 新建问题
    int insertUserCreateQuestion(Question question);

    // 保存用户编辑的问题
    int updateUserEditQuestion(Question question);
}