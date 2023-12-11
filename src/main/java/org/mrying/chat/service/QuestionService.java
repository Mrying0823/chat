package org.mrying.chat.service;

import org.mrying.chat.model.Question;

import java.util.List;
import java.util.Map;

/**
 * @author 邓和颖
 * @since 2023/12/11 12:04
 */
public interface QuestionService {

    // 根据查询条件分页查询问题列表
    List<Question> queryQuestionByConditionForPage(Map<String, Object> map);

    // 根据查询条件获取问题数量
    int queryCountOfQuestionByCondition(Map<String,Object> map);
}
